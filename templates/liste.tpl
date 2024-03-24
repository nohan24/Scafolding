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

@if ((int)ViewData["totalPages"] > 1)
{
    <ul class="pagination">
        @for (int i = 1; i <= (int)ViewData["totalPages"]; i++)
        {
            <li class="@(i == (int)ViewData["pageNumber"] ? "active" : "")">
                <a href="@Url.Action("Liste", new { page = i, pageSize = (int)ViewData["pageSize"] })">@i</a>
            </li>
        }
    </ul>
}

<form asp-action="Csv" method="get">
    <button type="submit">Télécharger CSV</button>
</form>

<form asp-action="Csv" method="post" enctype="multipart/form-data">
    <input type="file" name="file" />
    <button type="submit">Importer CSV</button>
</form>

<div>
    <a asp-action="Create">Ajouter un nouveau #modelName#</a>
</div>
