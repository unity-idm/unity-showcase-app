<#include "header.ftlh">
<div class="container-fluid">
   <div class="row">
      <#assign nav_selection = "billings">
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
                     <td class="align-middle">Owner:</td>
                     <td class="align-middle">${owner.name!""}  (${owner.email!""})</td>
                     <td> </td>
                  </tr>
                  <tr>
                     <td class="align-middle">Status:</td>
                     <td class="align-middle"><b> ${context.subscription.details.status}</b></td>
                     <@security.authorize access="hasAuthority('ACTIVE_ACCOUNT')">
                     <#if context.subscription.details.cancelDate??>(<b>Cancels</b> at  ${(context.subscription.details.cancelDate*1000)?number_to_datetime})</#if>  
                     <td class="align-middle">
                        <form action="/application/subscription/<#if context.subscription.details.cancelDate??>renew<#else>cancel</#if>" method="post">
                           <button class="btn btn-sm btn-primary" type="submit" id="button-addon2"><#if context.subscription.details.cancelDate??>Renew<#else>Cancel</#if> subscription</button>
                        </form>
                     </td>
                     </@security.authorize>
                  </tr>
                  <tr>
                     <td class="align-middle">Billing plan:</td>
                     <td class="align-middle"> <b> ${context.subscription.premium?string("Premium", "Light")} </b></td>
                     <#if !context.subscription.details.cancelDate??>
                     <td class="align-left"><a class="btn btn-sm btn-primary" href="${upgradeUrl}">Change plan</a></td>
                     </#if>	
                  </tr>
               </tbody>
            </table>
            <div class="row pt-3">
               <h4>Credit card</h4>
            </div>
            <table class="table-sm table-borderless">
               <tbody>
                  <tr>
                     <td class="align-middle">Credit card ending with ${context.subscription.details.cardLast4}</td>
                     <#if !context.subscription.details.cancelDate??>
                     <td class="align-middle"><a class="btn btn-sm btn-primary" href="${updateUrl}">Change credit card</a></td>
                     </#if>
                  </tr>
               </tbody>
            </table>
            <div class="row pt-0">
               <h4>Invoices</h4>
            </div>
            <table class="table table-striped table-sm"">
               <tbody>
               <thead>
                  <tr>
                     <th>Number</th>
                     <th>Created</th>
                     <th>Total</th>
                     <th>Status</th>
                     <th>Download</th>
                  </tr>
               </thead>
               <#list invoices as invoice>
               <tr>
                  <td class="align-middle">${invoice.number!""}</td>
                  <td class="align-middle">${(invoice.created*1000)?number_to_datetime}</td>
                  <td class="align-middle">${(invoice.total/100)?string.currency} ${invoice.currency?upper_case}</td>
                  <td class="align-middle">${invoice.status!""}</td>
                  <td class="align-middle">
                     <span class="glyphicon glyphicon-search"></span>
                     <a href="${invoice.url!""}"><#include "download_icon.ftlh"></a>
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
