<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">User profile</h1>
        <#include "logout.ftlh">
      </div>
      
      <div class="row p-4">

		<form>
		  <div class="col form-group">
		    <label for="firstName">First name</label>
		    <input type="text" class="form-control" id="firstName">
		  </div>
		  <div class="col form-group">
		    <label for="lastName">First name</label>
		    <input type="text" class="form-control" id="lastName">
		  </div>
		  <div class="col">
             <button type="submit" class="btn btn-primary">Update</button>
		  </div>
		</form>
		
      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
