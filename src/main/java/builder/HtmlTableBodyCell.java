package builder;

public class HtmlTableBodyCell implements IHtmlTableCell {

    private final String contents;

    HtmlTableBodyCell(final String contents) {
        this.contents = contents;
    }

    @Override
    public String build() {
        return "<td>" + this.contents + "</td>";
    }
}
