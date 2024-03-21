@model #project#.Models.VMLogin;
@{
    Layout = null;
}

<!DOCTYPE html>
    <html>
    <head>
    <link rel="stylesheet" href="~/lib/bootstrap/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="~/css/site.css" asp-append-version="true" />
    <link rel="stylesheet" href="~/#project#.styles.css" asp-append-version="true" />
    <title>Login</title>
    </head>
    <body>
    
    <div style="width: 50%; margin: auto; margin-top: 10%;">
    <form asp-action="Login" asp-controller="Access" method="post">
        <h1 class="h3 mb-3 fw-normal">Login</h1>

        @if(ViewData["ValidateMessage"] != null)
        {
            <p style="color: red;">@ViewData["ValidateMessage"]</p>
        }

        <div class="form-floating mb-3">
            <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com" asp-for="Email">
            <label for="floatingInput">Email address</label>
        </div>
        <div class="form-floating mb-3">
            <input type="password" asp-for="Password" class="form-control" id="floatingPassword" placeholder="Password">
            <label for="floatingPassword">Password</label>
        </div>

        <button class="btn btn-primary w-100 py-2" type="submit">Se connecter</button>
    </form>
    </div>
</body>
    </html>

