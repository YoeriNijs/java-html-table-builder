package builder;

import java.util.List;

public class HtmlTableHeader implements IHtmlTableElement {

    private final List<HtmlTableRow> rows;

    HtmlTableHeader(final List<HtmlTableRow> rows) {
        this.rows = rows;
    }

    @Override
    public String build() {
        final StringBuilder sb = new StringBuilder("<thead>");
        rows.stream()
                .map(HtmlTableRow::build)
                .forEach(sb::append);
        sb.append("</thead>");
        return sb.toString();
    }
}
