package deepfocus.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.List;

public class JsonPersistence {

    private static final ObjectMapper mapper = new ObjectMapper();
    
    static {
        // Necessário para LocalDateTime
        mapper.registerModule(new JavaTimeModule());
    }
    
    // Serialização (Java -> JSON)
    public static <T> void salvar(String caminho, List<T> lista) throws Exception {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(caminho), lista);
    }

    // Desserialização (JSON -> Java)
    public static <T> List<T> carregar(String caminho, Class<T> classe) throws Exception {
        File arquivo = new File(caminho);
        
        // Verificação da existência do arquivo
        if (!arquivo.exists()) {
            return List.of();
        }
        
        // Preparação do tipo da lista e desserialização
        CollectionType listaType =
            mapper.getTypeFactory().constructCollectionType(List.class, classe);
       
        return mapper.readValue(arquivo, listaType);
    }
}
