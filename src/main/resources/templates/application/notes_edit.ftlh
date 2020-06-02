<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#assign nav_selection = "notes">
	<#include "navigation.ftlh">
	<#if add || errorMessage??>
            <#assign urlAction>/application/notes/add</#assign>
            <#assign submitTitle>Create</#assign>
        <#else>
            <#assign urlAction>${'/application/note/' + note.id + '/edit'}</#assign>
            <#assign submitTitle>Update</#assign>
    </#if>
	

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Notes > New</h1>
        <#include "logout.ftlh">
      </div>
      
      <#if errorMessage??>
            <div class="alert alert-warning" role="alert">${errorMessage}</div>
   	  </#if>
      
      <div class="table-responsive p-4">
		<form action="${urlAction}" method="post">
		   <div class="form-group">
		    <label for="title">Note title</label>
		    <input class="form-control" id="title" type="text" name="title" value="${(note.title)!''}" />
		  </div>
		  
		  <div class="form-group">
		    <label for="content">Note content</label>
		    <textarea class="form-control" id="content" rows="3"  name="content" >${(note.content)!''}</textarea>
		  </div>
		 
		  <div class="form-group row">
		    <div class="col-sm-10">
		      <button type="submit" class="btn btn-sm btn-primary">${submitTitle}</button>
		    </div>
		  </div>
		</form>


      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
