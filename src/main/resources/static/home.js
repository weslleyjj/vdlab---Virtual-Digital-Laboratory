
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
