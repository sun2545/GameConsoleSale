var go = {};

document.addEventListener("DOMContentLoaded", main);

//create 3 canvas for charts
go.canvas1 = document.getElementById("canvasConsole1");
go.canvas2 = document.getElementById("canvasConsole2");
go.canvas3 = document.getElementById("canvasConsole3");

go.chart1 = null;
go.chart2 = null;
go.chart3 = null;

//main entry point
function main() {
    
    //default is "hardwareSales"
    requestJson("hardwareSales");
    
    Logger.open();
   
    //register click events for radios
    go.radioHardware = document.getElementById("radioHardware");

    //ananomous function
    go.radioHardware.addEventListener("click", function () {
        requestJson(go.radioHardware.value); //or == this.value       
    });

    go.radioSoftware = document.getElementById("radioSoftware");

    //ananomous function
    go.radioSoftware.addEventListener("click", function () {
        requestJson(go.radioSoftware.value); //or == this.value       
    });

    go.radioGameTitles = document.getElementById("radioGameTitles");

    //ananomous function
    go.radioGameTitles.addEventListener("click", function () {
        requestJson(go.radioGameTitles.value); //or == this.value       
    });

}

function requestJson(category) {
    
    // 1. create XMLHttpRequest object
    var xhr = new XMLHttpRequest();

    // 2. initialize XHR //ConsoleSales is the Servlet
    xhr.open("GET", "ConsoleSales?category=" + category);

    // 3. send request
    xhr.send();

    // 4. receive jason asyncronously when it is completed
    // equivalent to xhr.addEventListener("load", function(){});
    xhr.onload = function () {

        //parse JSON
        if (xhr.status == 200) {
            log("RAW: " + xhr.response);

            // 1. parse JSON String 
            var consoles = JSON.parse(xhr.response); //returns an array of objects



            // 2. update table
            updateTable(category, consoles);
            
            // 3. update chart
            updateChart(category, consoles);
            
        } else {
            log("[ERROR] Failed to request JSON");
        }
    }

}

function updateTable(category, consoles) {

    // change table heading
    var valueHeading = document.getElementById("valueHeading");
    if (category == "hardwareSales")
        valueHeading.textContent = "Hardware (millions)";
    else if (category == "softwareSales")
        valueHeading.textContent = "software (millions)";
    else if (category == "gameTitles")
        valueHeading.textContent = "game Titles";

    //update 2nd colume (value)
    //get <tbody>
    var tbody = document.getElementById("tableConsole").tBodies[0];
    tbody.innerHTML = "";

    //iterate all rows
    for (var i = 0; i < consoles.length; i++)
    {
        var tr = document.createElement("tr");
        tbody.appendChild(tr);
        var td1 = document.createElement("td");
        var td2 = document.createElement("td");
        tr.appendChild(td1);
        tr.appendChild(td2);
        td1.textContent = consoles[i].name;
        td2.textContent = consoles[i].value;
    }
}

function updateChart(category, consoles)
{
    var chartType = "";
    var colors = [];
    if (category == "hardwareSales")
    {
        chartType = "pie";
        colors = [
            "rgba(255, 99, 132, 1)",
            "rgba(153, 102, 255, 1)",
            "rgba(54, 162, 235, 1)",
            "rgba(127, 49, 66, 1)",
            "rgba(76.5, 51, 127, 1)",
            "rgba(27, 81, 117, 1)",  
            "rgba(85, 33, 44, 1)",
            "rgba(51, 34, 85, 1)",
            "rgba(18, 54, 78, 1)" 
        ];
    } else if (category == "softwareSales")
    {
        chartType = "doughnut";
        colors = [
            "rgba(255, 99, 132, 1)",
            "rgba(153, 102, 255, 1)",
            "rgba(54, 162, 235, 1)",
            "rgba(127, 49, 66, 1)",
            "rgba(76.5, 51, 127, 1)",
            "rgba(27, 81, 117, 1)", 
            "rgba(85, 33, 44, 1)",
            "rgba(51, 34, 85, 1)",
            "rgba(18, 54, 78, 1)"  
        ];
    } else if (category == "gameTitles")
    {
        chartType = "polarArea";
        colors = [
            "rgba(255, 99, 132, 1)",
            "rgba(153, 102, 255, 1)",
            "rgba(54, 162, 235, 1)",
            "rgba(127, 49, 66, 1)",
            "rgba(76.5, 51, 127, 1)",
            "rgba(27, 81, 117, 1)",
            "rgba(85, 33, 44, 1)",
            "rgba(51, 34, 85, 1)",
            "rgba(18, 54, 78, 1)" 
        ];
    }

    // build chart data
    var names1 = [];
    var values1 = [];
    var names2 = [];
    var values2 = [];
    var names3 = [];
    var values3 = [];

    // get table element
    var tbody = document.getElementById("tableConsole").tBodies[0];

    // get values and name for charts
    for (var i = 0; i < tbody.rows.length; ++i)
    {
        var row = tbody.rows[i];
        var name = row.cells[0].textContent;
        var console = findConsoleByName(name, consoles);

        if (console)
        {
            if ((i - 1) % 3 == 0)
            {
                names1.push(name);
                values1.push(console.value);
            } else if ((i - 1) % 3 == 1)
            {
                names2.push(name);
                values2.push(console.value);
            } else if ((i - 1) % 3 == 2)
            {
                names3.push(name);
                values3.push(console.value);
            }
        }

        // define default chart data
        var config1 =
                {
                    type: chartType,
                    data:
                            {
                                labels: names1,
                                datasets:
                                        [{
                                                borderWidth: 1,
                                                data: values1,
                                                backgroundColor: colors,
                                            }]
                            }
                };

        var config2 =
                {
                    type: chartType,
                    data:
                            {
                                labels: names2,
                                datasets:
                                        [{
                                                borderWidth: 1,
                                                data: values2,
                                                backgroundColor: colors,
                                            }]
                            }
                };

        var config3 =
                {
                    type: chartType,
                    data:
                            {
                                labels: names3,
                                datasets:
                                        [{
                                                borderWidth: 1,
                                                data: values3,
                                                backgroundColor: colors,
                                            }]
                            }
                };



        // remove the previous chart before creating new one
        if (go.chart1)
            go.chart1.destroy();
        if (go.chart2)
            go.chart2.destroy();
        if (go.chart3)
            go.chart3.destroy();


        // create new chart
        var context1 = go.canvas1.getContext("2d");
        go.chart1 = new Chart(context1, config1);

        var context2 = go.canvas2.getContext("2d");
        go.chart2 = new Chart(context2, config2);

        var context3 = go.canvas3.getContext("2d");
        go.chart3 = new Chart(context3, config3);
    }


//return search object using key and return it
    function findConsoleByName(name, consoles) {
        for (var i = 0, console; console = consoles[i]; i++)
        {
            if (name == console.name) //found
                return console;

        }
        return null; //not found
    }
}









