@model #modelName#

<h2>Modifier #modelName#</h2>

<form asp-action="Edit" method="post">

    #champs#

    <div class="form-group">
        <button type="submit" class="btn btn-primary">Modifier</button>
    </div>
</form>
