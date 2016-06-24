package ph.codeia.lerandomshit.leddit;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * This file is a part of the Le Random Shit project.
 */
public class JsonParsingTest {

    @SuppressWarnings("NewApi")
    private String read(String filename) throws IOException {
        InputStream in = getClass().getResourceAsStream(filename);
        assert in != null;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    @Test
    public void readFile() {
        try {
            assertTrue(read("/story.json").length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            fail("io error");
        }
    }

    @Test
    public void parseIntoStory() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Hn.Story story = mapper.readValue(read("/story.json"), Hn.Story.class);
        assertTrue(story != null);
        assertEquals(8863, story.getId());
        assertEquals("dhouston", story.getBy());
        // outside of the Post interface
        assertEquals(71, story.descendants);
    }
}
