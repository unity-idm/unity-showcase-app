<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#assign nav_selection = "users">
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Users</h1>
        <#include "logout.ftlh">
      </div>	
      	<#if invitationEmail??>
      		 <div class="alert alert-success" role="alert">Invitation successfull send to ${invitationEmail}</div>
      	</#if>
		<form action="/application/user/invite" method="post">
		<div class="input-group float-right col-md-4 pr-4 pl-4">
		  <input path="email" type="text" name="email" class="form-control" placeholder="email" aria-label="Recipient's username" aria-describedby="button-addon2">
		  <div class="input-group-append">
		    <button class="btn btn-primary" type="submit" id="button-addon2">Invite</button>
		  </div>
		</div>
		</form>
      <div class="table-responsive p-4">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Admin</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
           	 <#list users as user>
           	 <tr>
              <td class="align-middle">${user.name}</td>
              <td class="align-middle">${user.email}</td>
              <td class="align-middle">${user.admin?string("yes", "no")}</td>
              <td class="align-middle"><a href="${'/application/user/' + user.id + '/delete'}">Delete</a></td>
             </tr>
            </#list>
          </tbody>
        </table>	
      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
