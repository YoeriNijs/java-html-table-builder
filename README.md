# Secure html table builder
A convenient table builder to create plain html tables the correct way. Fast - and secure!

### Usage
```
final String html = new HtmlTableBuilder.Builder()
    .withHeader("hc1", "hc2", "hc3")
    .withBody("bc1", "bc2", "bc3")
    .withBody("bc4", "bc5", "bc6")
    .build();
    
Output:

<table>
    <thead>
        <tr>
            <th>hc1</th>
            <th>hc2</th>
            <th>hc3</th>
        </tr></thead>
    <tbody>
        <tr>
            <td>bc1</td>
            <td>bc2</td>
            <td>bc3</td>
        </tr>
        <tr>
            <td>bc4</td>
            <td>bc5</td>
            <td>bc6</td>
        </tr>
    </tbody>
</table>
```

It actually uses the OWASP Java Html Sanitizer to sanitize the server side rendered output. For instance:
```
final String html = new HtmlTableBuilder.Builder()
    .withHeader("<script>alert('evil code');</script>")
    .build();
    
Output:

<table>
    <thead>
        <tr><th></th></tr>
    </thead>
    <tbody>
    </tbody>
</table>"));
```

### Good to know
- At the moment, the table builder support one table class that you can set to style the table itself. The idea behind this is simple: most 
frontend frameworks require just one table class to be set on the table itself to style it properly. You can pass the custom table class like this:

```
new HtmlTableBuilder.Builder("my-custom-table-class").build();
```

- Input elements are partially supported. Only inputs with the attributes "type", "name", "value" and "class" are supported. Of course, you can
easily adjust this by adjusting the PolicyFactory in the table builder.
