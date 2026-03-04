package com.seuprojeto.epidemiologia.service;

import com.seuprojeto.epidemiologia.entity.*;
import com.seuprojeto.epidemiologia.enums.DoencaFonte;
import com.seuprojeto.epidemiologia.repository.*;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.seuprojeto.epidemiologia.utils.BrasilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DoencasImportService {

    private static final Logger logger = LoggerFactory.getLogger(DoencasImportService.class);

    private final EstadoRepository estadoRepository;
    private final DoencaRepository doencaRepository;
    private final IndicadorRepository indicadorRepository;
    private final PopulacaoEstadoRepository populacaoEstadoRepository;

    public DoencasImportService(EstadoRepository estadoRepository,
                                DoencaRepository doencaRepository,
                                IndicadorRepository indicadorRepository,
                                PopulacaoEstadoRepository populacaoEstadoRepository) {
        this.estadoRepository = estadoRepository;
        this.doencaRepository = doencaRepository;
        this.indicadorRepository = indicadorRepository;
        this.populacaoEstadoRepository = populacaoEstadoRepository;
    }
    @Async
    public void importarCsvDoencaAno(int ano, String doenca) throws Exception {
        DoencaFonte fonte = DoencaFonte.fromNome(doenca);
        String url = fonte.getBaseUrl() + String.valueOf(ano).substring(2) + ".csv.zip";
        logger.info("Iniciando download do CSV. Doença: {}, Ano: {}, URL: {}",
                fonte.getNome(), ano, url);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        try {
            HttpResponse<InputStream> response = client.send(
                    request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Erro ao consumir API. Status HTTP: " + response.statusCode());
            }

            boolean csvEncontrado = false;

            try (ZipInputStream zipInputStream =
                         new ZipInputStream(response.body())) {

                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {

                    if (!entry.isDirectory() && entry.getName().endsWith(".csv")) {
                        csvEncontrado = true;
                        processarCsv(zipInputStream, ano, fonte.getNome());
                    }

                    zipInputStream.closeEntry();
                }
            }

            if (!csvEncontrado) {
                throw new RuntimeException(
                        "Nenhum arquivo CSV encontrado no ZIP para " + fonte.getNome() + " - " + ano);
            }

            logger.info("Importação finalizada com sucesso. Doença: {}, Ano: {}",
                    fonte.getNome(), ano);

        } catch (HttpTimeoutException e) {
            logger.error("Timeout ao acessar API: {}", url, e);
            throw new RuntimeException("Timeout ao acessar fonte externa", e);

        } catch (IOException e) {
            logger.error("Erro de IO ao processar arquivo ZIP: {}", url, e);
            throw new RuntimeException("Erro ao processar arquivo da API", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrompida durante requisição HTTP", e);
            throw new RuntimeException("Execução interrompida", e);
        }
    }


    private void processarCsv(InputStream inputStream, int ano, String doenca) throws Exception {
        CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                .build();

        Map<String, Integer> casosAgregados = new HashMap<>();
        Map<String, Integer> obitosAgregados = new HashMap<>();

        String[] cabecalho = reader.readNext();
        if (cabecalho == null) throw new RuntimeException("CSV vazio!");
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < cabecalho.length; i++) {
            idx.put(cabecalho[i].trim().toUpperCase(), i);
        }

        int contador = 0;
        String[] colunas;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while ((colunas = reader.readNext()) != null) {
            String ufCodigo = getColuna(colunas, idx, "SG_UF_NOT");
            String uf = BrasilConstants.getSiglaPorCodigo(ufCodigo);
            String data = getColuna(colunas, idx, "DT_NOTIFIC");
            String evolucao = getColuna(colunas, idx, "EVOLUCAO");
            String dtObito = getColuna(colunas, idx, "DT_OBITO");

            if (uf == null || uf.equals("??") || data == null || data.isEmpty()) continue;

            int mes;
            try {
                mes = LocalDate.parse(data, formatter).getMonthValue();
            } catch (DateTimeParseException e) {
                logger.warn("Data inválida: {} na linha {}", data, contador + 1);
                mes = 0;
            }

            contador++;
            String chave = uf + "-" + ano + "-" + mes;
            casosAgregados.put(chave, casosAgregados.getOrDefault(chave, 0) + 1);

            if ((dtObito != null && !dtObito.isEmpty()) || "2".equals(evolucao)) {
                obitosAgregados.put(chave, obitosAgregados.getOrDefault(chave, 0) + 1);
            }

            if (contador % 1000 == 0) {
                logger.info("Linhas processadas: {}", contador);
            }
        }

        salvarIndicadores(casosAgregados, obitosAgregados, ano, doenca);
    }

    private String getColuna(String[] colunas, Map<String, Integer> idx, String nomeColuna) {
        Integer i = idx.get(nomeColuna.toUpperCase());
        return (i != null && i < colunas.length) ? colunas[i].trim() : null;
    }

    private void salvarIndicadores(Map<String, Integer> casosAgregados, Map<String, Integer> obitosAgregados, int ano, String doenca) {
        Doenca doencaEncontrada = doencaRepository.findByNome(doenca)
                .orElseThrow(() -> new RuntimeException("Doença " + doenca + " não encontrada"));

        for (String chave : casosAgregados.keySet()) {
            String[] partes = chave.split("-");
            if (partes.length < 3) continue;

            String uf = partes[0];
            int mes;
            try {
                mes = Integer.parseInt(partes[2]);
            } catch (NumberFormatException e) {
                mes = 0;
            }

            Estado estado = estadoRepository.findBySigla(uf)
                    .orElseThrow(() -> new RuntimeException("Estado não encontrado: " + uf));

            Integer casos = casosAgregados.getOrDefault(chave, 0);
            Integer obitos = obitosAgregados.getOrDefault(chave, 0);

            BigDecimal populacao = populacaoEstadoRepository
                    .findByEstadoIdAndAno(estado.getId(), ano)
                    .map(pop -> new BigDecimal(pop.getPopulacao()))
                    .orElse(BigDecimal.ONE);

            BigDecimal taxaPor100k = new BigDecimal(casos)
                    .divide(populacao, 6, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100000));

            indicadorRepository.upsertIndicador(
                    estado.getId().longValue(),
                    doencaEncontrada.getId().longValue(),
                    ano,
                    mes,
                    casos,
                    obitos,
                    taxaPor100k
            );
        }
    }
}
