// File: src/test/java/pl/put/poznan/transformer/logic/FilterKeysDecoratorUsingMockDelegateTest.java
package pl.put.poznan.transformer.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/** * Unit tests for the FilterKeysJsonTransformerDecorator using a mock delegate.
 * This class tests the behavior of the decorator when it interacts with a mocked JsonTransformer delegate.
 */
@ExtendWith(MockitoExtension.class)
class FilterKeysDecoratorUsingMockDelegateTest {

    @Mock
    private JsonTransformer mockDelegate;

    @Test
    void decoratorCallsDelegateOnceAndFiltersKeys() throws Exception {
        String inputJson = "{\"glossary\":{\"title\":\"some title\",\"ignored\":\"value\"}}";
        String delegateOutput = inputJson;
        when(mockDelegate.transform(inputJson)).thenReturn(delegateOutput);

        JsonTransformer decorator = new FilterKeysJsonTransformerDecorator(mockDelegate, Set.of("glossary", "title"));
        String result = decorator.transform(inputJson);

        verify(mockDelegate, times(1)).transform(inputJson);
        assertEquals("{\"glossary\":{\"title\":\"some title\"}}", result);
    }

    @Test
    void decoratorPropagatesDelegateException() throws Exception {
        String inputJson = "{\"glossary\":{\"title\":\"some title\",\"ignored\":\"value\"}}";
        RuntimeException originalException = new RuntimeException("Delegate failure");
        when(mockDelegate.transform(inputJson)).thenThrow(originalException);


        JsonTransformer decorator = new FilterKeysJsonTransformerDecorator(mockDelegate, Set.of("glossary", "title"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            decorator.transform(inputJson);
        });

        assertEquals(originalException.getMessage(), exception.getMessage());
        assertSame(originalException.getMessage(), exception.getMessage());
        verify(mockDelegate, times(1)).transform(inputJson);


    }


    @Test
    void decoratorCallsDelegateMultipleTimesForSameInput() throws Exception {
        String inputJson = "{\"glossary\":{\"title\":\"some title\",\"ignored\":\"value\"}}";
        when(mockDelegate.transform(inputJson)).thenReturn(inputJson);
        JsonTransformer decorator = new FilterKeysJsonTransformerDecorator(mockDelegate, Set.of("glossary", "title"));

        String result1 = decorator.transform(inputJson);
        String result2 = decorator.transform(inputJson);
        String result3 = decorator.transform(inputJson);

        verify(mockDelegate, times(3)).transform(inputJson);

        String expectedOutput = "{\"glossary\":{\"title\":\"some title\"}}";
        assertEquals(expectedOutput, result1);
        assertEquals(expectedOutput, result2);
        assertEquals(expectedOutput, result3);
    }

    @Test
    void decoratorHandlesDifferentJsonInputsSeparately() throws Exception {
        String inputJson1 = "{\"glossary\":{\"title\":\"first title\",\"ignored\":\"value1\"}}";
        String inputJson2 = "{\"glossary\":{\"title\":\"second title\",\"ignored\":\"value2\"}}";
        when(mockDelegate.transform(inputJson1)).thenReturn(inputJson1);
        when(mockDelegate.transform(inputJson2)).thenReturn(inputJson2);
        JsonTransformer decorator = new FilterKeysJsonTransformerDecorator(mockDelegate, Set.of("glossary", "title"));

        String result1 = decorator.transform(inputJson1);
        String result2 = decorator.transform(inputJson2);
        System.out.println(result1);
        System.out.println(result2);

        verify(mockDelegate, times(1)).transform(inputJson1);
        verify(mockDelegate, times(1)).transform(inputJson2);

        String expectedOutput1 = "{\"glossary\":{\"title\":\"first title\"}}";
        String expectedOutput2 = "{\"glossary\":{\"title\":\"second title\"}}";

        assertEquals(expectedOutput1, result1);
        assertEquals(expectedOutput2, result2);

        assertNotEquals(result1, result2);
    }


    @Test
    void sequentialCallsReturnDifferentFilteredResults() throws Exception {
        String inputJson = "{\"glossary\":{\"title\":\"test order\",\"ignored\":\"value\"}}";
        when(mockDelegate.transform(inputJson))
                .thenReturn("{\"glossary\":{\"title\":\"first title\",\"ignored\":\"value\"}}",
                        "{\"glossary\":{\"title\":\"second title\",\"ignored\":\"value\"}}");
        JsonTransformer decorator = new FilterKeysJsonTransformerDecorator(mockDelegate, Set.of("glossary", "title"));
        String firstResult = decorator.transform(inputJson);
        String secondResult = decorator.transform(inputJson);
        assertEquals("{\"glossary\":{\"title\":\"first title\"}}", firstResult);
        assertEquals("{\"glossary\":{\"title\":\"second title\"}}", secondResult);
        verify(mockDelegate, times(2)).transform(inputJson);
    }
}