<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#assign nav_selection = "userProfile">
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">User profile</h1>
        <#include "logout.ftlh">
      </div>
      
      <#if updated??>
      		 <div class="alert alert-success" role="alert">Profile updated</div>
      </#if>
      <div class="userProfileForm p-4">		
		<form action="/application/user" method="post">
		   <div class="form-group">
		    <label for="firstname">Firstname</label>
		    <input class="form-control" id="title" type="text" name="firstname" value="${(firstname)!''}" />
		  </div>
		  
		  <div class="form-group">
		    <label for="surname">Surname</label>
		    <input class="form-control" id="title" type="text" name="surname" value="${(surname)!''}" />
		  </div>
		 
		 <div class="form-group">
		    <label for="email">Email</label>
		    <input class="form-control" id="title" type="text" name="email" value="${(email)!''}" disabled/>
		  </div>
		 
		  <div class="form-group row">
		    <div class="col-sm-10">
		      <button type="submit" class="btn btn-sm btn-primary">Update</button>
		    </div>
		  </div>
		</form>
      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
