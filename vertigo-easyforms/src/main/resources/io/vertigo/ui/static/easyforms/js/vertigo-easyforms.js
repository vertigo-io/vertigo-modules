let context = document.currentScript.dataset.context ;

VUiExtensions.methods = {
    ...VUiExtensions.methods,
    
    // ****
    // * UI
    // ****
    
    efResolveFieldTypeLabel : function(fieldTypeName) {
		return this.$data.vueData.fieldTypes.find(e => e.name === fieldTypeName)?.label;
	},
    
    
    // ****
    // * Sections
    // ****
    efAddSection : function() {
		this.$data.componentStates.sectionModal.sectionIndex = -1;
        this.httpPostAjax(context + 'easyforms/designer/_addSection', { }, {
            onSuccess: function(response) {
                this.$data.componentStates.sectionModal.opened = true
            }.bind(this)
        });
    },
    
    efEditSection : function(sectionIndex) {
        this.$data.componentStates.sectionModal.sectionIndex = sectionIndex;
        this.httpPostAjax(context + 'easyforms/designer/_editSection', {sectionIndex: sectionIndex }, {
            onSuccess: function(response) {
                this.$data.componentStates.sectionModal.opened = true
            }.bind(this)
        });
    },
    
    efDeleteSection : function(sectionIndex) {
        this.httpPostAjax(context + 'easyforms/designer/_deleteSection', {sectionIndex: sectionIndex }, {
            onSuccess: function(response) {
                this.$q.notify({ message: response.data.model.message, type: 'positive' });
            }.bind(this)
        });
    },
    
    efMoveSection: function(fromSectionIndex, toSectionIndex) {
        this.httpPostAjax(context + 'easyforms/designer/_moveSection', {fromSectionIndex: fromSectionIndex, toSectionIndex: toSectionIndex },
        {
            onSuccess: function(response) {
                this.$q.notify({ message: response.data.model.message, type: 'positive' });
            }.bind(this)
        });
    },
    
    efSaveEditSection : function() {
        let formData = this.vueDataParams(['editSection','editLabelText.label']);
        formData.append('sectionIndex', this.$data.componentStates.sectionModal.sectionIndex);
        
        this.httpPostAjax(context + 'easyforms/designer/_saveSection', formData, {
            onSuccess: function(response) {
                this.$q.notify({ message: response.data.model.message , type: 'positive' });
                this.$data.componentStates.sectionModal.opened = false
            }.bind(this)
        });
    },

    // ****
    // * Items
    // ****
    efAddItem : function(type, sectionIndex, itemIndex) {
		this.$data.componentStates.itemModal.sectionIndex = sectionIndex;
        this.$data.componentStates.itemModal.editIndex = itemIndex || -1;
        this.$data.componentStates.itemModal.editIndex2 = (itemIndex == null ? '' : -1);
		this.$data.componentStates.itemModal.codeModified = false;
        this.httpPostAjax(context + 'easyforms/designer/_addItem', {type: type}, {
            onSuccess: function(response) {
                this.$data.componentStates.itemModal.opened = true
            }.bind(this)
        });
    },
    
    efDeleteItem : function(sectionIndex, editIndex, editIndex2) {
        this.httpPostAjax(context + 'easyforms/designer/_deleteItem', {sectionIndex: sectionIndex, editIndex: editIndex, editIndex2: editIndex2 }, {
            onSuccess: function(response) {
                this.$q.notify({ message: response.data.model.message, type: 'positive' });
            }.bind(this)
        });
    },


    efMoveItemRelative : function(sectionIndex, fromIndex, fromIndex2, offset) {
		let toIndex = (fromIndex2 == null ? fromIndex + offset : fromIndex);
		let toIndex2 = (fromIndex2 == null ? null : fromIndex2 + offset);
		this.efMoveItem(sectionIndex, fromIndex, fromIndex2, toIndex, toIndex2);
	},
	
    efMoveItem : function(sectionIndex, fromIndex, fromIndex2, toIndex, toIndex2) {
        this.httpPostAjax(context + 'easyforms/designer/_moveItem', {sectionIndex: sectionIndex,
                                                                     fromIndex: fromIndex, fromIndex2: fromIndex2,
                                                                     toIndex: toIndex, toIndex2: toIndex2 },
        {
            onSuccess: function(response) {
                this.$q.notify({ message: response.data.model.message, type: 'positive' });
            }.bind(this)
        });
    },
    
    efEditItem : function(sectionIndex, editIndex, editIndex2) {
        this.$data.componentStates.itemModal.sectionIndex = sectionIndex;
        this.$data.componentStates.itemModal.editIndex = editIndex;
        this.$data.componentStates.itemModal.editIndex2 = (editIndex2 == null ? '': editIndex2);
        this.$data.componentStates.itemModal.codeModified = true;
        this.httpPostAjax(context + 'easyforms/designer/_editItem', {sectionIndex: sectionIndex, editIndex: editIndex, editIndex2: editIndex2 }, {
            onSuccess: function(response) {
                this.$data.componentStates.itemModal.opened = true
            }.bind(this)
        });
    },
    
    
	// ****
	// * Item detail
	// ****
	
    efRefreshItem : function() {
		let formData = this.vueDataParams(['editItem']);
        formData.delete('vContext[editItem][type]')//not modifiable
		formData.delete('vContext[editItem][isDefault]')//not modifiable
		
		formData.append('sectionIndex', this.$data.componentStates.itemModal.sectionIndex);
		formData.append('doUpdateCode', !this.$data.componentStates.itemModal.codeModified);
		
        this.httpPostAjax(context + 'easyforms/designer/_refreshItem',formData);
    },


    efSaveEditItem : function() {
        let formData = this.vueDataParams(['editItem','editLabelText.label','editLabelText.text']);
        formData.delete('vContext[editItem][type]')//not modifiable
        formData.delete('vContext[editItem][isDefault]')//not modifiable
       
        formData.delete('vContext[editItem][parameters]') // specific field, need to be in json format
        formData.append('vContext[editItem][parameters]', JSON.stringify(this.vueData.editItem.parameters));
        
        formData.append('sectionIndex', this.$data.componentStates.itemModal.sectionIndex);
        formData.append('editIndex', this.$data.componentStates.itemModal.editIndex);
        formData.append('editIndex2', this.$data.componentStates.itemModal.editIndex2);
        
        this.httpPostAjax(context + 'easyforms/designer/_saveItem', formData, {
            onSuccess: function(response) {
                this.$q.notify({ message: response.data.model.message, type: 'positive' });
                this.$data.componentStates.itemModal.opened = false
            }.bind(this)
        });
    },

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
	
	// Function to test if last item is empty
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

