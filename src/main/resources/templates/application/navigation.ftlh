    <nav class="col-md-2 d-none d-md-block bg-light sidebar">
      <div class="sidebar-sticky">
        <ul class="nav flex-column">
          <li class="nav-item">
          	<div class="pb-2 pt-2 text-center">
          		<img class="mb-2" src="/images/logo.png" alt="" width="50">
          	</div>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if selected_nav == "NOTES">active</#if>" href="/application/notes">
              Notes
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if selected_nav == "USER_PROFILE">active</#if>" href="/application/user">
              User profile
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if selected_nav == "BILLINGS">active</#if>" href="/application/billings">
              Billings
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if selected_nav == "USERS">active</#if>" href="/application/users">
              Users
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link <#if selected_nav == "NEW_NOTEBOOK">active</#if>" href="/application/new">
              New subscription
            </a>
          </li>
        </ul>
      </div>
    </nav>