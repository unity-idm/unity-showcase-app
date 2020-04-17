    <nav class="col-md-2 d-none d-md-block bg-light sidebar">
      <div class="sidebar-sticky">
        <ul class="nav flex-column">
          <li class="nav-item">
          	<div class="pb-2 pt-2 text-center">
          		<img class="mb-2" src="/images/logo.png" alt="" width="50">
          	</div>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if nav_selection == "notes">active</#if>" href="/application/notes">
              Notes
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if nav_selection == "userProfile">active</#if>" href="/application/user">
              User profile
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if nav_selection == "billings">active</#if>" href="/application/billings">
              Billings
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if nav_selection == "users">active</#if>" href="/application/users">
              Users
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if nav_selection == "newSubscription">active</#if>" href="/application/new">
              New subscription
            </a>
          </li>
        </ul>
      </div>
    </nav>