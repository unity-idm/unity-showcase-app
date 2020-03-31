<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Users</h1>
        <#include "logout.ftlh">
      </div>
      
		<div class="input-group float-right col-md-4 pr-4 pl-4">
		  <input type="text" class="form-control" placeholder="email" aria-label="Recipient's username" aria-describedby="button-addon2">
		  <div class="input-group-append">
		    <button class="btn btn-primary" type="button" id="button-addon2">Invite</button>
		  </div>
		</div>
      <div class="table-responsive p-4">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>Email</th>
              <th>Role</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="align-middle">xyx1@at.org</td>
              <td class="align-middle">
				<select class="custom-select col-md-4">
		          <option>Admin</option>
		          <option>Regular</option>
				</select>
              </td>
              <td class="align-middle">delete</td>
            </tr>
            <tr>
              <td class="align-middle">xyx2@at.org</td>
              <td class="align-middle">
				<select class="custom-select col-md-4">
		          <option>Admin</option>
		          <option>Regular</option>
				</select>
              </td>
              <td class="align-middle">delete</td>
            </tr>
            <tr>
              <td class="align-middle">xyx3@at.org</td>
              <td class="align-middle">
				<select class="custom-select col-md-4">
		          <option>Admin</option>
		          <option>Regular</option>
				</select>
              </td>
              <td class="align-middle">delete</td>
            </tr>
          </tbody>
        </table>
		
      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
