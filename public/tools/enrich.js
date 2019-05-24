$(function () {
    goTable();
    keggTable();
    formValidation();
});

function change(element) {
    var value = $(element).find(">option:selected").val();
    if (value == "text") {
        $("#queryContent").show();
        $("#queryFile").hide()
    } else {
        $("#queryContent").hide();
        $("#queryFile").show()
    }
}

function getExample(species) {
    var example = "";
    switch (species) {
        case "02428":
            example = "Os02428G0508763500,Os02428G0101638500,Os02428G1020149900,Os02428G0304312000,Os02428G0407109800,\n" +
                "Os02428G0610068100,Os02428G0814442300,Os02428G0101429800,Os02428G1018835600,Os02428G0101239300,\n" +
                "Os02428G1224394600,Os02428G1019353800,Os02428G0407683600,Os02428G0611131800,Os02428G0101662200";
            break;
        case "9311":
            example = "Os9311G0100690000,Os9311G0610991600,Os9311G0101096000,Os9311G0713835000,Os9311G0612458900,\n" +
                "Os9311G0100687800,Os9311G0408755400,Os9311G0305255200,Os9311G0611348200,Os9311G0100143100";
            break;
        case "Basmati1":
            example = "OsBasmati1G0828702900,OsBasmati1G0101358100,OsBasmati1G0930455800,OsBasmati1G1239202100,\n" +
                "OsBasmati1G1136342800,OsBasmati1G0623380000,OsBasmati1G1136432400,OsBasmati1G0726475700,\n" +
                "OsBasmati1G1135106300,OsBasmati1G0980001940,OsBasmati1G0310427100,OsBasmati1G0416152900";
            break;
        case "CJ14":
            example = "OsCJ14G0201796800,OsCJ14G1018805900,OsCJ14G0814380500,OsCJ14G0101071000,OsCJ14G0406645200,\n" +
                "OsCJ14G1018889800,OsCJ14G0509747800,OsCJ14G0713764500,OsCJ14G0980001925,OsCJ14G0509027700,\n" +
                "OsCJ14G0180000208,OsCJ14G1121801400,OsCJ14G0202134400,OsCJ14G0100858100,OsCJ14G0712698200";
            break;
        case "CN1B":
            example = "OsCN1BG0723126500,OsCN1BG1029163900,OsCN1BG0721017100,OsCN1BG0101256800,OsCN1BG0516760300,\n" +
                "OsCN1BG0413672400,OsCN1BG0205667200,OsCN1BG0100726500,OsCN1BG0824948800,OsCN1BG0207671800,\n" +
                "OsCN1BG0928523100,OsCN1BG0412231800,OsCN1BG0309460500,OsCN1BG0928172300,OsCN1BG0620829700";
            break;
        case "D62B":
            example = "OsD62BG0309596200,OsD62BG1031097600,OsD62BG0827875500,OsD62BG1031488000,OsD62BG0208923500,\n" +
                "OsD62BG0415413700,OsD62BG1031754300,OsD62BG0309532800,OsD62BG0828223100,OsD62BG0205544700,\n" +
                "OsD62BG0517292100,OsD62BG0517505400,OsD62BG0205899700,OsD62BG0930075600,OsD62BG0313184300";
            break;
        case "DG":
            example = "OsDGG0511553000,OsDGG0101407800,OsDGG0408517700,OsDGG0920408700,OsDGG1125367100,\n" +
                "OsDGG0101581300,OsDGG0511411100,OsDGG0101174500,OsDGG0306494400,OsDGG0306552600,\n" +
                "OsDGG0101900600,OsDGG0306486800,OsDGG1180002154,OsDGG0716979700,OsDGG0716332300";
            break;
        case "DHX2H":
            example = "OsDHX2HG0304888700,OsDHX2HG0100715500,OsDHX2HG0201817900,OsDHX2HG1129105000,\n" +
                "OsDHX2HG0303807900,OsDHX2HG1232177600,OsDHX2HG1026261600,OsDHX2HG0100281400,\n" +
                "OsDHX2HG0719882100,OsDHX2HG0717068500,OsDHX2HG0203414600,OsDHX2HG0718729100";
            break;
        case "FH838":
            example = "OsFH838G1231506200,OsFH838G0305716600,OsFH838G0412170400,OsFH838G0100476100,\n" +
                "OsFH838G0101061500,OsFH838G0308227300,OsFH838G1232188800,OsFH838G0411582700,\n" +
                "OsFH838G0100320200,OsFH838G0719121300,OsFH838G0512965200,OsFH838G0925344600";
            break;
        case "FS32":
            example = "OsFS32G0509639500,OsFS32G0408721800,OsFS32G0304347100,OsFS32G1121581200,OsFS32G0614345000,\n" +
                "OsFS32G0480000943,OsFS32G0101737600,OsFS32G0306549800,OsFS32G0715316900,OsFS32G0716614100,\n" +
                "OsFS32G0509044600,OsFS32G0203782400,OsFS32G0817254000,OsFS32G0818280300,OsFS32G1225581100";
            break;
        case "G46":
            example = "OsG46G0814393500,OsG46G0101185100,OsG46G0917976300,OsG46G0510469700,OsG46G0612217600,\n" +
                "OsG46G0917083000,OsG46G0917973000,OsG46G0202457400,OsG46G0304869600,OsG46G0917686900,\n" +
                "OsG46G0101101200,OsG46G0203365800,OsG46G0916513900,OsG46G0203586300,OsG46G0101824000";
            break;
        case "Guang8B":
            example = "OsGuang8BG0613908800,OsGuang8BG1124421100,OsGuang8BG0820001900,OsGuang8BG0100909000,\n" +
                "OsGuang8BG0304863100,OsGuang8BG0612885000,OsGuang8BG0305871700,OsGuang8BG0100728200,\n" +
                "OsGuang8BG0304824900,OsGuang8BG0101232900,OsGuang8BG0303894000,OsGuang8BG0509605100";
            break;
        case "Gui630":
            example = "OsGui630G0609566800,OsGui630G1015649000,OsGui630G0915072900,OsGui630G0914784700,\n" +
                "OsGui630G0407664100,OsGui630G0304650300,OsGui630G0508928600,OsGui630G0609628700,\n" +
                "OsGui630G0100596800,OsGui630G0406613100,OsGui630G1218284400,OsGui630G0509359800";
            break;
        case "II32":
            example = "OsII32G0415185600,OsII32G1235852700,OsII32G1032062500,OsII32G0415322000,OsII32G1032125100,\n" +
                "OsII32G0416455600,OsII32G0725168000,OsII32G0518807600,OsII32G0620216200,OsII32G0101791600,\n" +
                "OsII32G0416306700,OsII32G0415725600,OsII32G0825556300,OsII32G0101255000,OsII32G0517718700";
            break;
        case "IR64":
            example = "OsIR64G0405956200,OsIR64G0612335600,OsIR64G0304422600,OsIR64G0407125800,OsIR64G1021453700,\n" +
                "OsIR64G0609455600,OsIR64G0101332100,OsIR64G0304164700,OsIR64G0406986800,OsIR64G0609690000,\n" +
                "OsIR64G0817545600,OsIR64G0406565300,OsIR64G1122655400,OsIR64G1021197600,OsIR64G0817278500";
            break;
        case "J4115S":
            example = "OsJ4115SG1223235100,OsJ4115SG0717415100,OsJ4115SG0919467100,OsJ4115SG1121749100,\n" +
                "OsJ4115SG0410191400,OsJ4115SG0203330900,OsJ4115SG0409236600,OsJ4115SG0304395300,\n" +
                "OsJ4115SG0303856800,OsJ4115SG0305615800,OsJ4115SG1223266300,OsJ4115SG0304608300";
            break;
        case "KY131":
            example = "OsKY131G0713578900,OsKY131G0280200827,OsKY131G1219044700,OsKY131G0306393800,\n" +
                "OsKY131G0408715300,OsKY131G0100893400,OsKY131G0816328500,OsKY131G0304884300,\n" +
                "OsKY131G0100452800,OsKY131G0715225000,OsKY131G0511281300,OsKY131G1118828300";
            break;
        case "lemont":
            example = "OslemontG0201776000,OslemontG0917441600,OslemontG1225170400,OslemontG0407825900,\n" +
                "OslemontG0101507500,OslemontG0203640500,OslemontG0712220700,OslemontG0202146100,\n" +
                "OslemontG0407704900,OslemontG0304272100,OslemontG0407249000,OslemontG0508839000";
            break;
        case "LJ":
            example = "OsLJG0407838300,OsLJG0407014200,OsLJG0201966700,OsLJG1121184100,OsLJG0100088700,OsLJG0303941700,\n" +
                "OsLJG0918509900,OsLJG1224146500,OsLJG0406422900,OsLJG1226918400,OsLJG0609984200,OsLJG0918520300,\n" +
                "OsLJG0304559600,OsLJG0100431000,OsLJG0508038500,OsLJG0712771600,OsLJG0100960300,OsLJG0406846300";
            break;
        case "N22":
            example = "OsN22G0410837500,OsN22G0618605500,OsN22G0101236700,OsN22G0823023800,OsN22G1231951300,\n" +
                "OsN22G0617646000,OsN22G1231334500,OsN22G0615858100,OsN22G0305695200,OsN22G0100014700,\n" +
                "OsN22G0615962300,OsN22G0514779600,OsN22G0412612300,OsN22G0201745000,OsN22G1233566100";
            break;
        case "NAMROO":
            example = "OsNAMROOG1128881600,OsNAMROOG0304243500,OsNAMROOG0304421100,OsNAMROOG0304735900,\n" +
                "OsNAMROOG0101666900,OsNAMROOG0613631900,OsNAMROOG0202723900,OsNAMROOG0719220300,\n" +
                "OsNAMROOG1231712000,OsNAMROOG1233203300,OsNAMROOG0305414700,OsNAMROOG0822802500";
            break;
        case "Nip":
            example = "OsNipG1144240500,OsNipG0420086600,OsNipG1248616700,OsNipG0730964500,OsNipG1144984500,\n" +
                "OsNipG0624886600,OsNipG0105963300,OsNipG0728862900,OsNipG1145691700,OsNipG0311552200,\n" +
                "OsNipG0100598200,OsNipG0520692700,OsNipG0938455700,OsNipG0315362500,OsNipG0524305900";
            break;
        case "R3551":
            example = "OsR3551G0921884500,OsR3551G0407732400,OsR3551G1022774600,OsR3551G0305805800,\n" +
                "OsR3551G0512435100,OsR3551G0819848100,OsR3551G0101164800,OsR3551G0718278300,\n" +
                "OsR3551G0101325400,OsR3551G0201729800,OsR3551G0716369900,OsR3551G0718342700";
            break;
        case "R498":
            example = "OsR498G0305119300,OsR498G1221065400,OsR498G0511339600,OsR498G0305912400,OsR498G0305754800,\n" +
                "OsR498G0714487100,OsR498G0409550300,OsR498G0305132300,OsR498G1220932600,OsR498G0102368600,\n" +
                "OsR498G0204103100,OsR498G0714951400,OsR498G0202864000,OsR498G0868303200,OsR498G0815527400";
            break;
        case "R527":
            example = "OsR527G1021026400,OsR527G0204043100,OsR527G0714356000,OsR527G0408516800,OsR527G0203333200,\n" +
                "OsR527G0714379000,OsR527G1122242500,OsR527G1224468300,OsR527G0613921900,OsR527G0612328400,\n" +
                "OsR527G0306322500,OsR527G0407485600,OsR527G0305099300,OsR527G0204178500,OsR527G0816494600";
            break;
        case "R548":
            example = "OsR548G1040467400,OsR548G0416400400,OsR548G0106840700,OsR548G0316084800,OsR548G0212178300,\n" +
                "OsR548G0521960000,OsR548G1143540000,OsR548G1039950800,OsR548G0834541100,OsR548G0627425400,\n" +
                "OsR548G0106747500,OsR548G0316085800,OsR548G0421138200,OsR548G0419398300,OsR548G1040355000";
            break;
        case "TUMBA":
            example = "OsTUMBAG0203644700,OsTUMBAG0819072200,OsTUMBAG0303837200,OsTUMBAG0714247900,\n" +
                "OsTUMBAG1124047200,OsTUMBAG0611122300,OsTUMBAG0101227300,OsTUMBAG0510423400,\n" +
                "OsTUMBAG0407070500,OsTUMBAG0920390500,OsTUMBAG0303841700,OsTUMBAG0510623900";
            break;
        case "WSSM":
            example = "OsWSSMG0712616900,OsWSSMG0101239300,OsWSSMG0101084900,OsWSSMG0406158500,\n" +
                "OsWSSMG0611198100,OsWSSMG0101088200,OsWSSMG0202099700,OsWSSMG0915715600,\n" +
                "OsWSSMG0101395100,OsWSSMG1017731100,OsWSSMG0915357600,OsWSSMG0509248500";
            break;
        case "Y58S":
            example = "OsY58SG0512137300,OsY58SG0921692500,OsY58SG1128210400,OsY58SG0101054800,OsY58SG0303795900,\n" +
                "OsY58SG0614306900,OsY58SG0304476000,OsY58SG0613224700,OsY58SG1026051200,OsY58SG0101597300,\n" +
                "OsY58SG0101115200,OsY58SG0202779900,OsY58SG0201745600,OsY58SG0304264800,OsY58SG0409400300";
            break;
        case "YueGuang":
            example = "OsYueGuangG0303841700,OsYueGuangG0821725100,OsYueGuangG0615946100,OsYueGuangG0409774800,\n" +
                "OsYueGuangG0100188900,OsYueGuangG0203155500,OsYueGuangG0100093800,OsYueGuangG0408698000,\n" +
                "OsYueGuangG0101547400,OsYueGuangG0100205800,OsYueGuangG0822289100,OsYueGuangG0617188300,\n" +
                "OsYueGuangG0100313100,OsYueGuangG0305128000,OsYueGuangG1028036600,OsYueGuangG0304248300";
            break;
        case "YX":
            example = "OsYXG0410169200,OsYXG1280002570,OsYXG1232923500,OsYXG0513225700,OsYXG0515498700,\n" +
                "OsYXG0720416300,OsYXG0101534600,OsYXG0721670900,OsYXG0412184900,OsYXG1129010900,\n" +
                "OsYXG1028049100,OsYXG0101205400,OsYXG0204506900,OsYXG0309200000,OsYXG0680001338";
            break;
        case "ZH11":
            example = "OsZH11G0614556100,OsZH11G0924084300,OsZH11G0924037600,OsZH11G0510894100,OsZH11G0508099300,\n" +
                "OsZH11G0923483000,OsZH11G0303878100,OsZH11G0923374400,OsZH11G0100572400,OsZH11G0923571700,\n" +
                "OsZH11G1231018300,OsZH11G1127231000,OsZH11G0203006500,OsZH11G0407044600,OsZH11G0717243500";
            break;
        default:
            example = "Os02428G0100000100,Os02428G0100000300,Os02428G0100000500";
    }
    return example;
}

$("#egGene").click(function () {
    var species = $("#db").val();
    var example = getExample(species);
    $("#gene").val(example);
});

$("#egFile").click(function () {
    var species = $("#db").val();
    var example = getExample(species);
    var e = "";
    $.each(example.trim().split(','),function (i,v) {
        e += v.trim() + "\n";
    });
    var fileName = "example.txt";
    var blob = new Blob([e], {
        type: "text/plain;charset=utf-8"
    });
    saveAs(blob, fileName)
});

function goTable() {
    var array = ["ID", "Enrichment", "Description", "Ratio_In_Study", "Ratio_In_Pop", "P_Uncorrected",
        "P_Bonferroni", "P_Holm", "P_Sidak", "P_Fdr", "Namespace", "Genes_In_Study"];
    var values = ["id", "enrichment", "description", "ratio_in_study", "ratio_in_pop", "p_uncorrected",
        "p_bonferroni", "p_holm", "p_sidak", "p_fdr", "namespace", "genes_in_study"];
    var html = "";
    $.each(array, function (n, value) {
            html += "<label style='margin-right: 15px'>" +
                "<input type='checkbox' checked='checked' value='" + values[n] + "' onclick=\"setGoColumns('" + values[n] + "')\">" + value +
                "</label>"
        }
    );
    $("#goCheckbox").append(html);

    $("#goTable").bootstrapTable();
    var hiddenArray = ["p_bonferroni", "p_holm", "p_sidak", "p_fdr", "genes_in_study"];
    $.each(hiddenArray, function (n, value) {
        $('#goTable').bootstrapTable('hideColumn', value);
        $("input:checkbox[value=" + value + "]").attr("checked", false)
    });
}

function setGoColumns(value) {
    var element = $("input:checkbox[value=" + value + "]");
    if (element.is(":checked")) {
        $('#goTable').bootstrapTable('showColumn', value);
    } else {
        $('#goTable').bootstrapTable('hideColumn', value);
    }
}

function keggTable() {
    var array = ["Database", "ID", "Input number", "Background number", "P-Value", "Corrected P-Value", "Input", "Hyperlink"];
    var values = ["database", "id", "input_num", "back_num", "p-value", "correct_pval", "input", "hyperlink"];
    var html = "";
    $.each(array, function (n, value) {
            html += "<label style='margin-right: 15px'>" +
                "<input type='checkbox' checked='checked' value='" + values[n] + "' onclick=\"setKeggColumns('" + values[n] + "')\">" + value +
                "</label>"
        }
    );
    $("#keggCheckbox").append(html);

    $('#keggTable').bootstrapTable();
    var hiddenArray = ["correct_pval", "input"];
    $.each(hiddenArray, function (n, value) {
        $('#keggTable').bootstrapTable('hideColumn', value);
        $("input:checkbox[value=" + value + "]").attr("checked", false)
    });
}

function setKeggColumns(value) {
    var element = $("input:checkbox[value=" + value + "]");
    if (element.is(":checked")) {
        $('#keggTable').bootstrapTable('showColumn', value);
    } else {
        $('#keggTable').bootstrapTable('hideColumn', value);
    }
}

function formValidation() {
    $('#form').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            pValue: {
                validators: {
                    notEmpty: {
                        message: 'This is not be empty!'
                    },
                    numeric: {
                        message: 'The p-value must be a number!'
                    }
                }
            }
        }
    });
}

function Running() {
    $("#goResult").hide();
    $("#keggResult").hide();
    var form = $("#form");
    var fv = form.data("formValidation");
    fv.validate();
    if (fv.isValid()) {
        $("#result").hide();
        var index = layer.load(1, {
            shade: [0.1, '#fff']
        });

        $("#run").attr("disabled", true).html("Running...");

        $.ajax({
            url: "/RiceRC/tools/enrichment",
            type: "post",
            dataType: "json",
            processData: false,
            contentType: false,
            data: new FormData($("#form")[0]),
            success: function (data) {
                if (data.valid == "false") {
                    swal("Error", data.message, "error");
                } else {
                    var method = $("#method").val();
                    $("#" + method + "Table").bootstrapTable("load", data);
                    $("#" + method + "Result").show();
                }
                $("#run").attr("disabled", false).html("Run").blur();
                layer.close(index)
            }
        });
    }
}
