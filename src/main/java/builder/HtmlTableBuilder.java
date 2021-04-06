package builder;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * A convenient table builder to create plain html tables the correct way. Fast - and secure!
 */
public class HtmlTableBuilder {

    public static class Builder {

        private final List<HtmlTableRow> headerRows = new ArrayList<>();
        private final List<HtmlTableRow> bodyRows = new ArrayList<>();

        private final PolicyFactory policyFactory;
        private final String tableClass;

        public Builder(final String withTableClass) {
            final PolicyFactory inputSanitizer = new HtmlPolicyBuilder()
                    .allowElements("input")
                    .allowAttributes("type", "name", "value", "class").onElements("input")
                    .toFactory();
            this.policyFactory = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(inputSanitizer);
            this.tableClass = withTableClass;
        }

        public Builder() {
            this(null);
        }

        public Builder withHeader(final String... cells) {
            final List<IHtmlTableCell> tableHeaderCells = Arrays.stream(cells)
                    .map(policyFactory::sanitize)
                    .map(HtmlTableHeaderCell::new)
                    .collect(toList());
            headerRows.add(new HtmlTableRow(tableHeaderCells));
            return this;
        }

        public Builder withBody(final String... cells) {
            final List<IHtmlTableCell> tableBodyCells = Arrays.stream(cells)
                    .map(policyFactory::sanitize)
                    .map(HtmlTableBodyCell::new)
                    .collect(toList());
            bodyRows.add(new HtmlTableRow(tableBodyCells));
            return this;
        }

        public String build() {
            if (containsInvalidCellLength()) {
                throw new UnsupportedOperationException(
                        "Mismatch in cell length! Headers and bodies must have the same amount of cells.");
            }

            final String cssClass = createCssClass();
            final String thead = new HtmlTableHeader(headerRows).build();
            final String tbody = new HtmlTableBody(bodyRows).build();
            return "<table" + cssClass + ">" + thead + tbody + "</table>";
        }

        private boolean containsInvalidCellLength() {
            final boolean validHeaderLength = headerRows.stream()
                    .allMatch(hRow -> bodyRows.stream()
                            .allMatch(bRow -> hRow.getCellLength() == bRow.getCellLength()));
            final boolean validBodyLength = bodyRows.stream()
                    .allMatch(bRow -> headerRows.stream()
                            .allMatch(hRow -> bRow.getCellLength() == hRow.getCellLength()));
            return !validHeaderLength || !validBodyLength;
        }

        private String createCssClass() {
            return tableClass == null ? "" : " class=\"" + tableClass + "\"";
        }
    }

    private HtmlTableBuilder() {
        // Do not instantiate
    }
}
