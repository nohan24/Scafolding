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
