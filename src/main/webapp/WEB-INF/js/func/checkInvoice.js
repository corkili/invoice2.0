function resetQuantity(input) {
    var value = input.value;
    if (value.indexOf('.') >= 0 || isNaN(Number(value)) || Number(value) <= 0) {
        input.value = "";
    }
}

function resetMoney(input) {
    var value = input.value;
    if (isNaN(Number(value)) || Number(value) <= 0) {
        input.value = "";
    }
}

function resetRate(input) {
    var value = input.value;
    if (isNaN(Number(value)) || Number(value) <= 0 || Number(value) > 1) {
        input.value = "";
    }
}

function determineDetailNumber(){
    var num = document.getElementById("detail_num").value;
    if(num > 0 && num < 17) {
        document.getElementById("btn_determine").disabled = true;
        var tmp = document.createElement("form");
        tmp.action = "add_invoice_hand";
        tmp.method = "post";
        tmp.style.display = "none";
        var opt = document.createElement("input");
        opt.name = "detail_num";
        opt.value = num;
        tmp.appendChild(opt);
        document.body.appendChild(tmp);
        tmp.submit();
        return tmp;
    } else {
        alert("发票明细数目应在1到16之间！");
    }
}

function $$(id) {
    return  document.getElementById(id);
}

var defaultTextColor = "#888888";

function checkInvoiceCode() {
    var input = $$('invoiceCode');
    var code = input.value;
    if (code.length == 10) {
        input.style.color = defaultTextColor;
        return true;
    } else {
        input.style.color = "#ff1327";
        return false;
    }
}

function checkInvoiceId() {
    var input = $$('invoiceId');
    var code = input.value;
    if (code.length == 8) {
        input.style.color = defaultTextColor;
        return true;
    } else {
        input.style.color = "#ff1327";
        return false;
    }
}

function checkQuantity(input) {
    var value = input.value;
    if (value.indexOf('.') >= 0 || isNaN(Number(value)) || Number(value) <= 0) {
        input.style.color = "#ff1327";
        return false;
    } else {
        input.style.color = defaultTextColor;
        return true;
    }
}

function checkMoney(input) {
    var value = input.value;
    if (isNaN(Number(value)) || Number(value) < 0) {
        input.style.color = "#ff1327";
        return false;
    } else {
        input.style.color = defaultTextColor;
        return true;
    }
}

function checkRate(input) {
    var value = input.value;
    if (isNaN(Number(value)) || Number(value) < 0 || Number(value) > 1) {
        input.style.color = "#ff1327";
        return false;
    } else {
        input.style.color = defaultTextColor;
        return true;
    }
}

function checkSubmitInvoice(num) {
    var quantities = [];
    var taxRates = [];
    var moneys = [];
    var i;
    for (i = 0; i < num; i++) {
        moneys.push(document.getElementById("unitPrice_" + i));
        moneys.push(document.getElementById("amount_" + i));
        moneys.push(document.getElementById("taxSum_" + i));
        taxRates.push(document.getElementById("taxRate_" + i));
        quantities.push(document.getElementById("quantity_" + i));
    }
    moneys.push(document.getElementById("totalAmount"));
    moneys.push(document.getElementById("totalTax"));
    moneys.push(document.getElementById("total"));

    var successful = checkInvoiceCode() && checkInvoiceId();

    for (i = 0; i < quantities.length; i++) {
        successful = checkQuantity(quantities[i]);
    }

    for (i = 0; i < moneys.length; i++) {
        successful = checkMoney(moneys[i]);
    }

    for (i = 0; i < taxRates.length; i++) {
        successful = checkRate(taxRates[i]);
    }

    if (successful) {
        return confirm('是否确认提交？');
    } else {
        alert("某些信息输入不正确，请检查红字部分！");
        return false;
    }
}
