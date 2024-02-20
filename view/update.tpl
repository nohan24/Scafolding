@model #modelName#

<h2>Modifier #modelName#</h2>

<form asp-action="Edit" method="post">

    #champs#
    <br/>
    <div class="form-group">
        <input type="hidden" asp-for="#id#" value="@Model.#id#"/>
        <button type="submit" class="btn btn-primary">Modifier</button>
    </div>
</form>
