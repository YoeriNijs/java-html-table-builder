package builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class HtmlTableBuilderTest {

    @Nested
    class Styling {

        @Test
        void withNoHeadersAndNoBodyAndNoStyle_thenExpectEmptyTableWithoutClass() {
            final String html = new HtmlTableBuilder.Builder().build();
            assertThat(html, is("<table><thead></thead><tbody></tbody></table>"));
        }

        @Test
        void withNoHeadersAndNoBodyAndStyle_thenExpectEmptyTableWithClass() {
            final String html = new HtmlTableBuilder.Builder("table-class").build();
            assertThat(html, is("<table class=\"table-class\"><thead></thead><tbody></tbody></table>"));
        }
    }

    @Nested
    class THead {

        @Test
        void withHeadersInOneRow_thenExpectTableWithHeaders() {
            final String html = new HtmlTableBuilder.Builder()
                    .withHeader("header1", "header2", "header3")
                    .build();
            assertThat(html,
                       is("<table><thead><tr><th>header1</th><th>header2</th><th>header3</th></tr></thead><tbody"
                                  + "></tbody></table>"));
        }

        @Test
        void withMultipleHeaderCalls_thenExpectMultipleHeaderRows() {
            final String html = new HtmlTableBuilder.Builder()
                    .withHeader("header1", "header2", "header3")
                    .withHeader("header4", "header5", "header6")
                    .build();
            assertThat(html,
                       is("<table><thead><tr><th>header1</th><th>header2</th><th>header3</th></tr><tr><th>header4</th"
                                  + "><th>header5</th><th>header6</th></tr></thead><tbody></tbody></table>"));
        }
    }

    @Nested
    class TBody {

        @Test
        void withBodyInOneRow_thenExpectTableWithBody() {
            final String html = new HtmlTableBuilder.Builder()
                    .withBody("cell1", "cell2", "cell3")
                    .build();
            assertThat(html,
                       is("<table><thead></thead><tbody><tr><td>cell1</td><td>cell2</td><td>cell3</td></tr></tbody"
                                  + "></table>"));
        }

        @Test
        void withMultipleBodyCalls_thenExpectMultipleBodyRows() {
            final String html = new HtmlTableBuilder.Builder()
                    .withBody("cell1", "cell2", "cell3")
                    .withBody("cell4", "cell5", "cell6")
                    .build();
            assertThat(html,
                       is("<table><thead></thead><tbody><tr><td>cell1</td><td>cell2</td><td>cell3</td></tr><tr><td"
                                  + ">cell4</td><td>cell5</td><td>cell6</td></tr></tbody></table>"));
        }

        @Test
        void whenBuildFullTable_thenExpectValidTable() {
            final String html = new HtmlTableBuilder.Builder("custom-class")
                    .withHeader("hc1", "hc2", "hc3")
                    .withBody("bc1", "bc2", "bc3")
                    .withBody("bc4", "bc5", "bc6")
                    .build();
            assertThat(html, is("<table class=\"custom-class\"><thead><tr><th>hc1</th><th>hc2</th><th>hc3</th"
                                        + "></tr></thead><tbody><tr><td>bc1</td><td>bc2</td><td>bc3</td></tr><tr><td"
                                        + ">bc4</td><td>bc5</td><td>bc6</td></tr></tbody></table>"));
        }

        @Test
        void whenBuildFullTableWithInvalidBodyCellLength_thenExpectUnsupportedOperationException() {
            final HtmlTableBuilder.Builder builder = new HtmlTableBuilder.Builder(
                    "custom-class")
                    .withHeader("hc1", "hc2", "hc3")
                    .withBody("bc1", "bc2", "bc3")
                    .withBody("bc4");
            final UnsupportedOperationException unsupportedOperationException = Assertions
                    .assertThrows(UnsupportedOperationException.class, builder::build);
            assertThat(unsupportedOperationException.getMessage(),
                       is("Mismatch in cell length! Headers and bodies must have the same amount of cells."));
        }

        @Test
        void whenBuildFullTableWithInvalidHeaderCellLength_thenExpectUnsupportedOperationException() {
            final HtmlTableBuilder.Builder builder = new HtmlTableBuilder.Builder(
                    "custom-class")
                    .withHeader("hc1", "hc2")
                    .withBody("bc1", "bc2", "bc3");
            final UnsupportedOperationException unsupportedOperationException = Assertions
                    .assertThrows(UnsupportedOperationException.class, builder::build);
            assertThat(unsupportedOperationException.getMessage(),
                       is("Mismatch in cell length! Headers and bodies must have the same amount of cells."));
        }
    }

    @Nested
    class XssAttacks {

        @Test
        void whenInsertingScriptInHead_thenItShouldBeEscaped() {
            final String html = new HtmlTableBuilder.Builder()
                    .withHeader("<script>alert('evil code');</script>")
                    .build();
            assertThat(html, is("<table><thead><tr><th></th></tr></thead><tbody></tbody></table>"));
        }

        @Test
        void whenInsertingScriptInBody_thenItShouldBeEscaped() {
            final String html = new HtmlTableBuilder.Builder()
                    .withBody("<script>alert('evil code');</script>")
                    .build();
            assertThat(html, is("<table><thead></thead><tbody><tr><td></td></tr></tbody></table>"));
        }

        @Test
        void whenInsertingValidInput_thenItShouldBeAccepted() {
            final String html = new HtmlTableBuilder.Builder()
                    .withBody("<input type=\"submit\" name=\"aName\" class=\"a-class\" />")
                    .build();
            assertThat(html, is("<table><thead></thead><tbody><tr><td><input type=\"submit\" name=\"aName\" "
                                        + "class=\"a-class\" /></td></tr></tbody></table>"));
        }

        @Test
        void whenInsertingInputWithInvalidField_thenItShouldBeStripped() {
            final String html = new HtmlTableBuilder.Builder()
                    .withBody("<input type=\"submit\" name=\"aName\" class=\"a-class\" id=\"notSupported\" />")
                    .build();
            assertThat(html, is("<table><thead></thead><tbody><tr><td><input type=\"submit\" name=\"aName\" "
                                        + "class=\"a-class\" /></td></tr></tbody></table>"));
        }
    }

}
