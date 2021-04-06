package builder;

import java.util.List;

public class HtmlTableBody implements IHtmlTableElement {

    private final List<HtmlTableRow> rows;

    HtmlTableBody(final List<HtmlTableRow> rows) {
        this.rows = rows;
    }

    @Override
    public String build() {
        final StringBuilder sb = new StringBuilder("<tbody>");
        rows.stream()
                .map(HtmlTableRow::build)
                .forEach(sb::append);
        sb.append("</tbody>");
        return sb.toString();
    }
}
