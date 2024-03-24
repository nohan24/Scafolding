using Microsoft.AspNetCore.Mvc;
using #project#.Models;
using System.Collections.Generic;
using System.Text;

namespace #namespace#{
    public class #modelName#Controller : Controller
    {
        public IActionResult Liste(int page = 1, int pageSize = 10)
        {
            #modelName# objInstance = new #modelName#();
            List<#modelName#> all = objInstance.getAll();
            var paginatedData = all.Skip((page - 1) * pageSize).Take(pageSize);
            ViewData["data"] = paginatedData;
            ViewData["pageNumber"] = page;
            ViewData["pageSize"] = pageSize;
            ViewData["totalItems"] = all.Count;
            return View();
        }

        
        public IActionResult Create()
        {
            #modelName# obj = new #modelName#();
            #fk#
            return View();
        }

        [HttpPost]
        public IActionResult Create(#modelName# obj)
        {
            obj.insert();
            return RedirectToAction("Liste");
        }

        public IActionResult Update(int id)
        {
            #modelName# objInstance = new #modelName#();
            #modelName# obj = objInstance.getById(id);
            #fk#
            return View(obj);
        }

        [HttpPost]
        public IActionResult Edit(#modelName# obj)
        {
            obj.update();
            return RedirectToAction("Liste");
        }

        public IActionResult Delete(int id)
        {
            #modelName# objInstance = new #modelName#();
            objInstance.delete(id);
            return RedirectToAction("Liste");
        }

        [HttpGet]
        public IActionResult Csv()
        {
            #modelName# e = new #modelName#();
            List<#modelName#> data = e.getAll();
            var csvContent = new StringBuilder();
            csvContent.AppendLine("#cols#");
            foreach (var d in data)
            {
                csvContent.AppendLine($"#data#");
            }
            string fileName = "#modelName#.csv";
            byte[] byteArray = Encoding.UTF8.GetBytes(csvContent.ToString());
            return File(byteArray, "text/csv", fileName);
        }

          [HttpPost]
            public IActionResult Csv(IFormFile file)
            {
                try
                {
                    if (file != null && file.Length > 0)
                    {
                        if (Path.GetExtension(file.FileName).ToLower() == ".csv")
                        {
                            #modelName# e = new #modelName#();
                            e.importCsv(file);
                        }
                    }
                    
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }
                return Redirect("Liste");
            }
    }
}
