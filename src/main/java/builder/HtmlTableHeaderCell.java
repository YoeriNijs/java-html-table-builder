package builder;

class HtmlTableHeaderCell implements IHtmlTableCell {

    private final String contents;

    HtmlTableHeaderCell(final String contents) {
        this.contents = contents;
    }

    @Override
    public String build() {
        return "<th>" + this.contents + "</th>";
    }

}
