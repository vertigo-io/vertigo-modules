let context = document.currentScript.dataset.context ;

VUiExtensions.methods = {
    ...VUiExtensions.methods,
    efHasFormError : function(uiMessageStack, object, champ) {
    	return (uiMessageStack.objectFieldErrors[object] != null && uiMessageStack.objectFieldErrors[object]['form_'+ champ] != null)
    },
	
    efGetFormError : function(uiMessageStack, object, champ) {
    	  return (uiMessageStack.objectFieldErrors[object] != null && uiMessageStack.objectFieldErrors[object]['form_'+ champ] != null &&
    	  			 uiMessageStack.objectFieldErrors[object]['form_'+ champ].join(', '));
    },

    efAddItem : function() {
        this.httpPostAjax(context + 'easyforms/designer/_addItem', {}, {
            onSuccess: function(response) {
                this.$data.componentStates.itemModal.editIndex = -1;
                this.$data.componentStates.itemModal.opened = true
            }.bind(this)
        });
    },

    efEditItem : function(editIndex) {
        this.$data.componentStates.itemModal.editIndex = editIndex;
        this.httpPostAjax(context + 'easyforms/designer/_editItem', { editIndex: editIndex }, {
            onSuccess: function(response) {
                this.$data.componentStates.itemModal.opened = true
            }.bind(this)
        });
    },
    
    efRefreshItem : function() {
        this.httpPostAjax(context + 'easyforms/designer/_refreshItem',{'fieldType':this.vueData.editField.fieldType});
    },


    efSaveEditItem : function() {
        let formData = this.vueDataParams(['editField']);
        formData.delete('vContext[editField][isDefault]')//champ non modifiable
        if (!formData.has('vContext[editField][fieldValidators]')) {
            formData.append('vContext[editField][fieldValidators]', '')
        }
        formData.append('editIndex', this.$data.componentStates.itemModal.editIndex)
        this.httpPostAjax(context + 'easyforms/designer/_saveItem', formData, {
            onSuccess: function(response) {
                this.$q.notify({ message: 'Element validé', type: 'positive' });
                this.$data.componentStates.itemModal.opened = false
            }.bind(this)
        });
    },

    efDeleteItem : function(editIndex) {
        this.$data.componentStates.itemModal.editIndex = editIndex;
        this.httpPostAjax(context + 'easyforms/designer/_deleteItem', { editIndex: editIndex }, {
            onSuccess: function(response) {
                this.$q.notify({ message: 'Element supprimé', type: 'positive' });
            }.bind(this)
        });
    },

    efMoveItem : function(editIndex, offset) {
        this.httpPostAjax(context + 'easyforms/designer/_moveItem', { editIndex: editIndex, offset: offset }, {
            onSuccess: function(response) {
                this.$q.notify({ message: 'Element déplacé', type: 'positive' });
            }.bind(this)
        });
    },
    
    /** When used without itemModal */
    efAddItemNoModal: function(listName) {
        this.httpPostAjax(context + 'easyforms/designer/_addItem', this.vueDataParams([listName]), { });
    },
    efDeleteItemNoModal: function(listName, editIndex) {
        let formData = this.vueDataParams([listName]);
        formData.append('editIndex', editIndex);
        this.httpPostAjax(context + 'easyforms/designer/_deleteItem', formData, {
            onSuccess: function(response) {
                this.$q.notify({ message: 'Element supprimé', type: 'positive' });
            }.bind(this)
         });
    },
    efMoveItemNoModal: function(listName, editIndex, offset) {
        let formData = this.vueDataParams([listName]);
        formData.append('editIndex', editIndex);
        formData.append('offset', offset);
        this.httpPostAjax(context + 'easyforms/designer/_moveItem', formData, {            
        });
    },
    
    /** When used inner vueData */
    efAddItemVueData: function() {
        this.$data.vueData.motifValues.push({label:''});
    },
    efDeleteItemVueData: function(editIndex) {
        this.$data.vueData.motifValues.splice(editIndex, 1);
    },
    efMoveItemVueData: function(editIndex, offset) {
	    let element = this.$data.vueData.motifValues[editIndex];
	    this.$data.vueData.motifValues.splice(editIndex, 1);
	    this.$data.vueData.motifValues.splice(editIndex + offset, 0, element);
    }
}

window.addEventListener('vui-before-plugins', function(event) {
	
	let vuiEasyForms = Vue.defineComponent({
		props: {
			modelValue: { type: Object, required: true },
		},
		data: function() {
			return {
				formulaire :  {}
			}
		},
		template: `
			<div>
				<slot v-bind:formulaire="formulaire" >
				</slot>
			</div>
		`
		,
		emits: ["update:modelValue"],
		created: function() {
			if(this.$props.modelValue) {
				this.$data.formulaire = this.$props.modelValue
			} else {
				this.$data.formulaire = {}
			}
		},
		watch: {
			modelValue: function(newVal) {
				this.$data.formulaire = newVal;
			},
			formulaire: {
				handler: function(newVal) {
					this.$emit('update:modelValue', this.$data.formulaire);
				},
				deep: true
			},
		}
	})
	event.detail.vuiAppInstance.component('vui-easy-forms',vuiEasyForms)
});

