<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#assign nav_selection = "notes">
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Notes > New</h1>
        <#include "logout.ftlh">
      </div>
      
      <div class="table-responsive p-4">

		<form action="/application/notes" method="post">
		  <div class="form-group">
		    <label for="exampleFormControlTextarea1">Note content</label>
		    <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
		  </div>
		  <div class="form-group row">
		    <div class="col-sm-10">
		      <button type="submit" class="btn btn-primary">Add</button>
		    </div>
		  </div>
		</form>


      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
