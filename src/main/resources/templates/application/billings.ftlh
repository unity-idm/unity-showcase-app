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
      <table class="table-sm table-borderless">
      	<tbody>
      		<tr>
      			<td class="align-middle">Ownwer:</td>
      			<td class="align-middle">Company Name</td>
      		</tr>
      		<tr>
      		    <td class="align-middle">Subscription is Active</td>
      			<td class="align-middle"><a class="btn btn-sm btn-outline-secondary" href="#">Cancel</a></td>
      		</tr>
      		<tr>
      		    <td class="align-middle">You have Lite plan</td>
      			<td class="align-middle"><a class="btn btn-sm btn-outline-secondary" href="#">Upgrade</a></td>
      		</tr>
      	</tbody>
      </table>
      	
      <div class="row pt-3">
		<h4>Credit card</h4> 
      </div>
      <table class="table table-sm table-borderless">
      	<tbody>
      		<tr>
      			<td class="align-middle">Mastercard ending with 1234</td>
      		</tr>
      		<tr>
      			<td class="align-middle"><a class="btn btn-sm btn-outline-secondary" href="#">Change</a></td>
      		</tr>
      	</tbody>
      </table>
      

      <div class="row pt-0">
		<h4>Invoices</h4>
      </div>
      <table class="table-sm table-borderless">
      	<tbody>
      		<tr>
      			<td class="align-middle">Jan 31, 2020</td>
      			<td class="align-middle">XYX/30/22</td>
      			<td class="align-middle">$99</td>
      			<td class="align-middle">Paid</td>
      			<td class="align-middle">
  					<span class="glyphicon glyphicon-search"></span>
      				<a href="#"><#include "download_icon.ftlh"></a>
      			</td>
      		</tr>
      		<tr>
      			<td class="align-middle">Feb 28, 2020</td>
      			<td class="align-middle">XYX/31/22</td>
      			<td class="align-middle">$99</td>
      			<td class="align-middle">Paid</td>
      			<td class="align-middle">
  					<span class="glyphicon glyphicon-search"></span>
      				<a href="#"><#include "download_icon.ftlh"></a>
      			</td>
      		</tr>
      		<tr>
      			<td class="align-middle">Mar 31, 2020</td>
      			<td class="align-middle">XYX/33/22</td>
      			<td class="align-middle">$99</td>
      			<td class="align-middle">Paid</td>
      			<td class="align-middle">
  					<span class="glyphicon glyphicon-search"></span>
      				<a href="#"><#include "download_icon.ftlh"></a>
      			</td>
      		</tr>
      		<tr>
      		</tr>
      	</tbody>
      </table>
      
      </div> 
		
		
    </main>
  </div>
</div>

<#include "footer.ftlh">
