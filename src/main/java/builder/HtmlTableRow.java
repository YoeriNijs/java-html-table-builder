package builder;

import java.util.List;

public class HtmlTableRow implements IHtmlTableElement {

    private final List<IHtmlTableCell> cells;

    HtmlTableRow(final List<IHtmlTableCell> cells) {
        this.cells = cells;
    }

    @Override
    public String build() {
        final StringBuilder sb = new StringBuilder("<tr>");
        cells.stream()
                .map(IHtmlTableCell::build)
                .forEach(sb::append);
        sb.append("</tr>");
        return sb.toString();
    }

    int getCellLength() {
        return cells.size();
    }
}
