<!DOCTYPE html>
<html>
    <head>
        <title>Code</title>
        <style>
            #load_date {
                color: green;
                font: 12px;

            }
            #code_snippet {
                border: 2px inset grey;
                background-color: ivory;
            }
        </style>
        <link rel="stylesheet"
               href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
        <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
        <script>hljs.initHighlightingOnLoad();</script>
    </head>
    <body>
        <span id="load_date">${date}</span>
        <br>
        <span id="time_restriction">The code will be available for <b>${time}</b> seconds</span>
        <pre id="code_snippet">
            <code>${code}</code>
        </pre>
    </body>
</html>