<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	<#assign nav_selection = "newSubscription">
	<#include "navigation.ftlh">
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">New subscription</h1>
        <#include "logout.ftlh">
      </div>  
      <div class="table-responsive p-4">
		<a class="btn btn btn-primary" href="${registrationUrl}">Register new subscription</a>	
      </div>
    </main>
  </div>
</div>
<#include "footer.ftlh">
