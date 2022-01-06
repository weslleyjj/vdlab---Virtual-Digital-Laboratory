
$(document).ready(() => {
   if($("#operacaoSucesso").length > 0){
       Swal.fire({
           icon: 'success',
           title: 'Operação realizada com sucesso!',
           showConfirmButton: false,
           timer: 1500
       })
   }
});

function excluir(idAgendamento){
    const baseurl = window.location.origin;

    Swal.fire({
        icon: 'question',
        title: 'Excluir agendamento?',
        showDenyButton: true,
        confirmButtonText: `Excluir`,
        denyButtonText: `Cancelar`
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url : baseurl+"/agendamento/deletar/"+idAgendamento,
                type : 'get',

                success: function (response){
                    Swal.fire({
                        icon: 'success',
                        title: 'Agendamento excluido',
                        text: 'O agendamento foi excluido',
                        confirmButtonText: `OK`,
                    }).then((result) => {
                        window.location.reload();
                    })
                },

                error: function (xhr, textStatus, error){
                    console.log(xhr)
                    Swal.fire({
                        icon: 'error',
                        title: 'Ooops...',
                        text: 'Houve um problema interno e o agendamento não pôde ser excluido',
                    })
                }
            })
        }
    })
}
