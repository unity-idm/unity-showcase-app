<#include "header.ftlh">

<div class="container-fluid">
  <div class="row">
	
	<#include "navigation.ftlh">
	
    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 p-0">
    
      <div class="p-4 d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Billings</h1>
        <#include "logout.ftlh">
      </div>
      
      <div class="p-4 container">
      
      <div class="row">
		<h4>Subscription</h4>
      </div>
      
      <table class="table table-sm table-borderless">
      	<tbody>
      		<tr>
      		    <td>Status:</td>
      			<td class="col-1">Active</td>
      		</tr>
      		<tr>
      		    <td>Type:</td>
      			<td>Light <a class="btn btn-outline-secondary" href="#">Upgrade</a></td>
      		</tr>
      	</tbody>
      </table>
      	
      	
      <div class="row pl-3">
			<a class="btn btn-outline-secondary" href="#">Cancel</a> 
			
      </div>
		1. Information about subscription
		5. Cancel subscription/upgrade subscription/downgrade subscritpion table-borderless
		
      <div class="row pt-3">
		<h4>Credit card</h4>
		2. stripped information about credit card/ possibility to change credit card
      </div>
		
      <div class="row pt-3">
		<h4>Invoices</h4>
      </div>
		
      </div> 
		
		
    </main>
  </div>
</div>

<#include "footer.ftlh">
