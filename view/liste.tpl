@{
    ViewData["Title"] = "Liste #modelName#";
}

<h1>Liste #modelName#</h1>

<table class="table">
    <thead>
        <tr>
            #HeaderColumns#
        </tr>
    </thead>
    <tbody>
        @foreach (var item in ViewData["data"] as List<#modelName#>)
        {
            <tr>
                #RowColumns#
            </tr>
        }
    </tbody>
</table>

<div>
    <a asp-action="Create">Ajouter un nouveau #modelName#</a>
</div>
