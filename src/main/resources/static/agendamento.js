
$(document).ready(() => {
    $("#formAgendamento").hide();
    if($("#warning-erros").length > 0){
        escolherUsuario($("#formCampoUsuario").val(), {value: $("#nomeUsuarioRetorno").val()});
    }
})

$("#busca-usuario-form").on('submit',(e) => {
    e.preventDefault();
    const valorBusca = $("#buscaUsuario").val();
    if(valorBusca.trim().length > 0){
        window.location.href = "/agendamento/buscaUsuario?nome="+valorBusca;
    } else {
        window.location.href = "/agendamento";
    }
})

$("#formAgendamento").submit((e) => {
   if($("#formCampoUsuario").val() == ''){
       e.preventDefault();
   }
});

function escolherUsuario(idUsuario, btn){
    $("#formCampoUsuario").val(idUsuario);
    $("#selectUsuario").val(btn.value);
    $("#nomeUsuarioRetorno").val(btn.value);
    $("#formAgendamento").show();
    $("#tabelaUsuarios").hide();
}

function escolherOutroUsuario(){
    $("#tabelaUsuarios").show();
    $("#formAgendamento").hide();
    $("#selectUsuario").val('');
    $("#formCampoUsuario").val(null);
    $("#warning-erros").hide();
}

$("#confirmar-agendamento").on('click', () => {
    Swal.fire({
        icon: 'question',
        title: 'Confirmar o agendamento?',
        html: getDadosAgendamento(),
        showDenyButton: true,
        confirmButtonText: `Cadastrar`,
        denyButtonText: `Cancelar`
    }).then((result) => {
        if (result.isConfirmed) {
            document.getElementById("formAgendamento").submit();
        }
    })
});

function getDadosAgendamento(){
    let texto = '';
    let dataAgendada = $("#formCampoData").val().split('-');
    let diaHora = dataAgendada[2].split('T');
    let dataFormatada = diaHora[0] + '/' + dataAgendada[1] + '/' + dataAgendada[0] + ' às ' + diaHora[1];
    let tempoSessao = $("#selectTempoSessao").val();

    texto += '<p> Usuário escolhido: \n';
    texto += $("#selectUsuario").val() + '</p>';
    texto += '<p> Horário: \n';
    texto += dataFormatada + '</p>';
    texto += '<p> Tempo de sessão: \n';
    texto += tempoSessao == 0 ? 'Tempo livre' : tempoSessao + ' Minutos'  + '</p>';

    return texto;
}
