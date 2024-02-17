namespace #packageName#;

using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

[Route("api/[controller]")]
[ApiController]
public class #ClassName#Controller : ControllerBase
{
    private readonly DbContext _dbContext;

    public #ClassName#Controller(DbContext dbContext)
    {
        _dbContext = dbContext;
    }

    [HttpGet("{id}")]
    public ActionResult<#ClassName#> Get#ClassName#ById(int id)
    {
        var #className# = _dbContext.#ClassName#s.Find(id);

        if (#className# == null)
        {
            return NotFound();
        }

        return #className#;
    }

    [HttpPost]
    public ActionResult<#ClassName#> Create#ClassName#([FromBody] #ClassName# #className#)
    {
        _dbContext.#ClassName#s.Add(#className#);
        _dbContext.SaveChanges();

        return CreatedAtAction(nameof(Get#ClassName#ById), new { id = #className#.Id }, #className#);
    }

    [HttpPut("{id}")]
    public IActionResult Update#ClassName#(int id, [FromBody] #ClassName# #className#)
    {
        var existing#ClassName# = _dbContext.#ClassName#s.Find(id);

        if (existing#ClassName# == null)
        {
            return NotFound();
        }
        _dbContext.SaveChanges();

        return NoContent();
    }

    [HttpDelete("{id}")]
    public IActionResult Delete#ClassName#(int id)
    {
        var #className#ToDelete = _dbContext.#ClassName#s.Find(id);

        if (#className#ToDelete == null)
        {
            return NotFound();
        }

        _dbContext.#ClassName#s.Remove(#className#ToDelete);
        _dbContext.SaveChanges();

        return NoContent();
    }

    [HttpGet]
    public ActionResult<IEnumerable<#ClassName#>> GetAll#ClassName#s()
    {
        var all#ClassName#s = _dbContext.#ClassName#s.ToList();

        return Ok(all#ClassName#s);
    }
}
