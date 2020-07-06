<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#assign nav_selection = "notes">
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Notes</h1>
        <#include "logout.ftlh">
      </div>
      <#if errorMessage??>
            <div class="alert alert-warning" role="alert">${errorMessage}</div>
   	  </#if>
      <div class="float-right pr-4">
	      <a class="btn btn-primary" href="/application/notes/new">New</a>
      </div>
          
      <div class="table-responsive p-4">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>Note title</th>
              <th>Note content</th>
              <th class="notesAction">Actions</th>
            </tr>
          </thead>
          <tbody>
              <#list notes as note>
              	<tr>
              		<td>${note.title!''}</td>
              		<td>${note.content}</td>
           			<td> <a href="${'/application/note/' + note.id + '/edit'}">Edit</a> |
                   		 <a href="${'/application/note/' + note.id + '/delete'}">Delete</a>
                    </td> 
              	</tr>
              </#list>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
