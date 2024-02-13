let context = document.currentScript.dataset.context ;

VUiExtensions.methods = {
    ...VUiExtensions.methods,

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
        formData.delete('vContext[editField][isDefault]')//not modifiable
       
        formData.delete('vContext[editField][parameters]') // specific field, need to be in json format
        formData.append('vContext[editField][parameters]', JSON.stringify(this.vueData.editField.parameters));
        
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
	
	// ****
	// * main component to handle JSON serialization
	// ****
	
	let vuiEasyForms = Vue.defineComponent({
		props: {
			modelValue: { type: Object, required: true },
		},
		data: function() {
			return {
				formData :  {}
			}
		},
		template: `
			<div>
				<slot v-bind:formData="formData" >
				</slot>
			</div>
		`
		,
		emits: ["update:modelValue"],
		created: function() {
			if(this.$props.modelValue) {
				this.$data.formData = this.$props.modelValue
			} else {
				this.$data.formData = {}
			}
		},
		watch: {
			modelValue: function(newVal) {
				this.$data.formData = newVal;
			},
			formData: {
				handler: function(newVal) {
					this.$emit('update:modelValue', this.$data.formData);
				},
				deep: true
			},
		}
	});
	event.detail.vuiAppInstance.component('vui-easy-forms',vuiEasyForms);
	
	// ****
	// * Map type input. Eg : Configure custom lists
	// ****
	
	// Function to test if last element is empty
	let isLastEmpty = o => {
		if (o == null) {
			return false;
		}
		const lastElem = o.slice(-1)[0];
		return !(lastElem.label?.length > 0 || lastElem.value?.length > 0);
	};
	
	let vuiEasyFormsMap = Vue.defineComponent({
		props: {
			modelValue: { type: Object, required: true },
			valueLabel: { type: String, default: 'Value'},
			labelLabel: { type: String, default: 'Label'},
		},
		data: function() {
			return {
				internalModel :  {}
			}
		},
		template: `
			<div>
				<div v-for="param in modelValue" class="row q-col-gutter-md">
					<q-input label-slot stack-label orientation="vertical" class="col-5"
							:label="valueLabel"
							v-model="param.value"
						></q-input>
					<q-input label-slot stack-label orientation="vertical" class="col-7"
							:label="labelLabel"
							v-model="param.label"
						></q-input>
				</div>
			</div>
		`
		,
		emits: ["update:modelValue"],
		created: function() {
			if(this.$props.modelValue) {
				this.$data.internalModel = this.$props.modelValue;
				if (!isLastEmpty(this.$props.modelValue)) {
					this.$data.internalModel.push({});
				}
			} else {
				this.$data.internalModel = [{}];
			}
		},
		watch: {
			modelValue: function(newVal) {
				if(this.$props.modelValue) {
					this.$data.internalModel = this.$props.modelValue;
					if (!isLastEmpty(this.$props.modelValue)) {
						this.$data.internalModel.push({});
					}
				} else {
					this.$data.internalModel = [{}];
				}
			},
			internalModel: {
				handler: function(newVal) {
					if (!isLastEmpty(this.$data.internalModel)) {
						this.$data.internalModel.push({});
					}
					this.$emit('update:modelValue', this.$data.internalModel);
				},
				deep: true
			},
		}
	});
	
	event.detail.vuiAppInstance.component('vui-ef-map',vuiEasyFormsMap);
});

