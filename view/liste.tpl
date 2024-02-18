@model #modelName#

<h1>#ViewTitle#:</h1>

<table class="table">
    <thead>
        <tr>
            #HeaderColumns#
        </tr>
    </thead>
    <tbody>
        @foreach (var item in Model)
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
