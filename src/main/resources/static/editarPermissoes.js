
$(document).ready(() => {
    if($("#operacaoSucesso").length > 0){
        Swal.fire({
            icon: 'success',
            title: 'Operação realizada com sucesso!',
            showConfirmButton: false,
            timer: 1500
        })
    }
    $("#formPermissoes").hide();
    if($("#warning-erros").length > 0){
        escolherUsuario($("#formCampoUsuario").val(), {value: $("#nomeUsuarioRetorno").val()});
    }
})

$("#busca-usuario-form").on('submit',(e) => {
    e.preventDefault();
    const valorBusca = $("#buscaUsuario").val();
    if(valorBusca.trim().length > 0){
        window.location.href = "/usuario/buscaUsuario?nome="+valorBusca;
    } else {
        window.location.href = "/usuario/permissoes";
    }
})

$("#formPermissoes").submit((e) => {
    if($("#formCampoUsuario").val() == ''){
        e.preventDefault();
    }

    if($("#divPermissoes input:checked").length <= 0){
        e.preventDefault();
        $("#spanError").show();
    }
});

function escolherUsuario(idUsuario, btn){
    $("#formCampoUsuario").val(idUsuario);
    $("#selectUsuario").val(btn.value);
    $("#nomeUsuarioRetorno").val(btn.value);
    $("#formPermissoes").show();
    $("#tabelaUsuarios").hide();
}

function escolherOutroUsuario(){
    $("#tabelaUsuarios").show();
    $("#formPermissoes").hide();
    $("#selectUsuario").val('');
    $("#formCampoUsuario").val(null);
    $("#warning-erros").hide();
}
