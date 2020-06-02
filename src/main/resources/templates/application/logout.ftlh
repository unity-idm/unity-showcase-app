      	<div class="d-flex bd-highlight">
 			<div class="p-2 flex-fill align-self-center bd-highlight">Logged as  <@security.authentication property="name"> </@security.authentication>
 			   <@security.authorize access="hasAuthority('ADMIN')"> (Admin) </@security.authorize> </div> 
 			   <@security.authorize access="hasAuthority('PREMIUM')"> <span style="font-size:200%;color:yellow;">&starf;</span>  </@security.authorize> 
			 
  			<div class="p-2 flex-fill align-self-center bd-highlight">
  				 <div class="btn-toolbar mb-2 mb-md-0">
				<a class="btn btn-outline-primary" href="/logout">Log out</a>
        		</div>
        	</div>
  		</div>    	
      	