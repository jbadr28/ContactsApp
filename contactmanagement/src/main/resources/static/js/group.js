/*!
    * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2023 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});


const draggables = document.querySelectorAll('.draggable');
const containers = document.querySelectorAll('.group-list');
const selectedGroups = document.getElementById('selectedGroups');
const availableGroups = document.getElementById('availableGroups');
const selectedGroupIdsInput = document.getElementById('selectedGroupIds');
draggables.forEach(draggable => {
	draggable.addEventListener('dragstart',() => {
		event.dataTransfer.setData('text/plain', event.target.getAttribute('value'));
		draggable.classList.add('dragging');
	})
})
draggables.forEach(draggable => {
	draggable.addEventListener('dragend',() => {
/*		console.log(draggable);
		const checkbox = draggable.querySelector("input[type='checkbox']");
		const value = checkbox.value;
		console.log(value);*/
		draggable.classList.remove('dragging');
	})
})
containers.forEach(container => {
	container.addEventListener('dragover',event=>{
		event.preventDefault();
		const currentdraggable = document.querySelector('.dragging');
		const afterElement = getDragAfterElement(container,event.clientY);
		
		if(afterElement==null){
			container.appendChild(currentdraggable);
			
		}else{
			container.insertBefore(currentdraggable,afterElement);
		}
		
		
	});
});
containers.forEach(container =>{
	container.addEventListener('drop', (event) => {
  	event.preventDefault();
	const contact =  document.getElementById('contactId');
	const contactId = contact.getAttribute('value');
	console.log('contact id '+contactId);

  console.log('Container:', event.currentTarget['id']);
  const kids = event.currentTarget.querySelectorAll('.draggable');
  kids.forEach(kid => {
	  console.log(kid);
	  console.log(kid.querySelectorAll('input'));
	  const kidinput = kid.querySelectorAll('input');
	  kidinput.forEach(inp=> {
		  if(event.currentTarget['id']=="selectedGroups"){
			  const groupId = inp.getAttribute('value')
			  console.log('add group '+groupId+' to contact '+contactId);
			  const baseUrl = '/contacts/addgroup';
  			  const url = baseUrl + '?contactId=' + encodeURIComponent(contactId) + '&groupId=' + encodeURIComponent(groupId);
			  fetch(url, {
  				method: 'POST'
				}).then(response => response.json())
				.then(result => {
    			console.log('Response from server:', result);
  				}).catch(error => {
    			console.error('Error:', error);
  				});
		  }else if(event.currentTarget['id']=="availableGroups"){
			  const groupId = inp.getAttribute('value')
			  console.log('add group '+groupId+' to contact '+contactId);
			  const baseUrl = '/contacts/deletegroup';
			  const url = baseUrl + '?contactId=' + encodeURIComponent(contactId) + '&groupId=' + encodeURIComponent(groupId);
			  fetch(url, {
  				method: 'DELETE'
				}).then(response => response.json())
				.then(result => {
    			console.log('Response from server:', result);
  				}).catch(error => {
    			console.error('Error:', error);
  				});
		  }
		  
	  })
  })
	});
});
/*const form = document.querySelector('form');
form.addEventListener('submit', () => {
    const selectedGroupIds = [];
    const selectedGroups = document.querySelectorAll('#selectedGroups li');
    selectedGroups.forEach(group => {
        selectedGroupIds.push(group.getAttribute('value'));
    });
    selectedGroupIdsInput.value = selectedGroupIds.join(',');
    console.log(selectedGroupIdsInput);
});*/
function getDragAfterElement(container,y){
	const draggableElements = [...container.querySelectorAll('.draggable:not(.dragging)')];
	return draggableElements.reduce((closest,child) => {
		const box = child.getBoundingClientRect();
		
		const offset = y-box.top - box.height/2 ;
		if(offset<0 && offset>closest.offset){
			return {offset:offset,element:child}
		}else{
			return closest;
		}
	},{offset:Number.NEGATIVE_INFINITY}).element;
	
}
