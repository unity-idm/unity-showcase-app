<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Select subscription</title>

    <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/css/bootstrap.min.css" />

    <!-- Custom styles for this template -->
    <link href="/css/login.css" rel="stylesheet">
  </head>

  <body class="text-center">
    <form class="form-signin" action="/selected-subscription" method="post">
      <img class="mb-4" src="/images/logo.png" alt="" width="70">
      <h1 class="h3 mb-3 font-weight-normal">Select subscription</h1>

      <select name="subscription" class="form-control mb-4" id="tenant" autofocus required">
        <#list subs?keys as sub>
            <option value="${sub}">${subs[sub]}</option>
        </#list>
      </select>
      <button class="btn btn-lg btn-primary btn-block" type="submit">Continue</button>
      <p class="mt-5 mb-3 text-muted">&copy; 2020</p>
    </form>
  </body>
</html>
