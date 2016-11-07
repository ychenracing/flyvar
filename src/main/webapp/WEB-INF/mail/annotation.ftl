<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <head>
            <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
            <meta content="width=device-width, height=device-height, initial-scale=1.0" name="viewport"/>
            <link href="static/css/buttons.css" rel="stylesheet"/>
            <link href="static/css/bootstrap.css" rel="stylesheet"/>
            <link href="static/css/bootstrap-theme.css" rel="stylesheet"/>
            <link href="static/css/flyvar.css" rel="stylesheet"/>
            <link href="static/css/font-awesome.min.css" rel="stylesheet"/>
            <link href="static/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
            <script src="static/js/jquery-2.2.4.js"></script>
            <script src="static/js/plugins/canvas-to-blob.js" type="text/javascript"></script>
            <script src="static/js/plugins/sortable.js" type="text/javascript"></script>
            <script src="static/js/plugins/purify.js" type="text/javascript"></script>
            <script src="static/js/fileinput.js"></script>
            <script src="static/js/bootstrap.js"></script>
            <script src="static/js/bootstrap-waitingfor.js"></script>
            <script src="static/js/plugins/theme.js"></script>
        </head>
    </head>
    <body>
        <div class="container">
            <#assign flyvarUrl = "${domain}/flyvar">
            <div class="row" style="text-align: left;">
                <span>
                    Dear user,
                </span>
            </div>
            <div class="row" style="text-align: left;">
                <p style="text-indent: 2em;">
                    Listed below please find your variation annotation result(s) from FlyVar.
                </p>
            </div>
            <div class="row" style="text-align: left;">
                <p style="text-indent: 2em;">
                    <a href="${flyvarUrl}">
                        <img class="width100" src="cid:flyvar-index"/>
                    </a>
                </p>
            </div>
            <div class="row" style="text-align: left;">
                <p style="text-indent: 2em;">
                    Click the links to download annotate results:
                    <ul>
                    <#if annovarInput??>
                        <li><a href="${flyvarUrl}/annotate/result/${annovarInput}">annovar input</a></li>
                    </#if>
                    <#if annotateResult??>
                        <#if combinedExonicResult??>
                            <li><a href="${flyvarUrl}/annotate/result/${combinedExonicResult}">variant function</a></li>
                        <#else>
                            <li><a href="${flyvarUrl}/annotate/result/${annotateResult}">variant function</a></li>
                            <#if exonicAnnotateResult??>
                                <li><a href="${flyvarUrl}/annotate/result/${exonicAnnotateResult}">exonic variant function</a></li>
                            </#if>
                        </#if>
                    </#if>
                    <#if combineAnnovarOut??>
                        <li><a href="${flyvarUrl}/annotate/result/${combineAnnovarOut}">combined
                                            annovar output</a></li>
                    </#if>
                    <#if annovarInvalidInput??>
                        <li><a href="${flyvarUrl}/annotate/result/${annovarInvalidInput}">invalid input for annovar</a></li>
                    </#if>
                    </ul>
                </p>
            </div>
            <div class="row" style="text-align: left;">
                <p style="text-indent: 2em;">
                    <strong>We will preserve you data for 30 days. We suggest download your results as soon as possible.</strong>
                </p>
            </div>
            <div class="row" style="text-align: right;">
                <p>
                    FlyVar,
                </p>
            </div>
            <div class="row" style="text-align: right;">
                <p>
                    ${.now?string("yyyy-MM-dd HH:mm:ss")}
                </p>
            </div>
        </div>
    </body>
</html>
