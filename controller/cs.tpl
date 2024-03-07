using Microsoft.AspNetCore.Mvc;
using #project#.Models;
using System.Collections.Generic;
using Sysyem.Text;

namespace #namespace#{
    public class #modelName#Controller : Controller
    {
        public IActionResult Liste()
        {
            #modelName# objInstance = new #modelName#();
            List<#modelName#> all = objInstance.getAll();
            ViewData["data"] = all;
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
                csvContent.AppendLine($"{d.Id};{d.Nom};{d.Date_naissance}");
            }
            string fileName = "#modelName#.csv";
            byte[] byteArray = Encoding.UTF8.GetBytes(csvContent.ToString());
            return File(byteArray, "text/csv", fileName);
        }
    }
}
