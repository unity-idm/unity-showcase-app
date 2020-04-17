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
      
      <div class="float-right pr-4">
	      <a class="btn btn-primary" href="/application/notes/new">New</a>
      </div>
          
      <div class="table-responsive p-4">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th>Note content</th>
              <th>Checked</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. De carne lumbering animata corpora quaeritis. Summus brains sit​​, morbo vel maleficia? De apocalypsi gorger omero undead survivor dictum mauris.</td>
              <td><input type="checkbox"></td>
              <td>delete edit</td>
            </tr>
            <tr>
              <td>Hi mindless mortuis soulless creaturas, imo evil stalking monstra adventus resi dentevil vultus comedat cerebella viventium.</td>
              <td><input type="checkbox"></td>
              <td>delete edit</td>
            </tr>
            <tr>
              <td><del>Qui animated corpse, cricket bat max brucks terribilem incessu zomby. The voodoo sacerdos flesh eater, suscitat mortuos comedere carnem virus. Zonbi tattered for solum oculi eorum defunctis go lum cerebro. Nescio brains an Undead zombies. Sicut malus putrid voodoo horror. Nigh tofth eliv ingdead.</del></td>
              <td><input type="checkbox" checked></td>
              <td>delete edit</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</div>

<#include "footer.ftlh">
