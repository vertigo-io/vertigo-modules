let context = document.currentScript.dataset.context ;

VUiExtensions.methods = {
    ...VUiExtensions.methods,
	
	efRoundOrDefaut : function(value, decimals, defaultValue) {
		if (!Number.isFinite(value)) {
			return defaultValue;
		}
		const factor = Math.pow(10, decimals);
		return Math.round((value + Number.EPSILON) * factor) / factor;
	},
    
    // ****
    // * UI
    // ****
    
    efResolveFieldTypeLabel : function(fieldTypeName) {
        return this.$data.vueData.fieldTypes.find(e => e.name === fieldTypeName)?.label;
    },
    
    efCheckUploadConstraints : function(object, field, actualFileCount, maxFileCount, tooManyFilesMessage, actualSize, maxSize, tooBigMessage, conserveErrors = false) {
        let errors = conserveErrors ? [...this.$data.uiMessageStack.objectFieldErrors?.[object]?.[field] || []] : [];
        
        if (maxFileCount != null && actualFileCount > maxFileCount) {
            if (!errors.includes(tooManyFilesMessage)) {
                errors.push(tooManyFilesMessage);
            }
        }
        
        if (maxSize != null && actualSize / 1024 / 1024 > maxSize) {
            if (!errors.includes(tooBigMessage)) {
                errors.push(tooBigMessage);
            }
        }
        
        
        // set error
        if (!this.$data.uiMessageStack.objectFieldErrors[object]) {
            this.$data.uiMessageStack.objectFieldErrors[object] = {};
        }
        this.$data.uiMessageStack.objectFieldErrors[object][field] = errors;
    },
    
    efAlertRejected(errors, extensionErrorMessage, sizeErrorMessage) {
        let errorMessages = [];
        for (let error of errors) {
            if (error.failedPropValidation === 'accept') {
                errorMessages.push(extensionErrorMessage);
            } else if (error.failedPropValidation === 'max-file-size') {
                errorMessages.push(sizeErrorMessage);
            }
        };
        this.$q.notify(this.uiMessageStackToNotify({globalErrors:errorMessages})[0])
    },
    
    efDecodeDate: function (value, format) {
        if (value === Quasar.date.formatDate(Quasar.date.extractDate(value, 'YYYY-MM-DD'), 'YYYY-MM-DD')) {
            return Quasar.date.formatDate(Quasar.date.extractDate(value, 'YYYY-MM-DD'), format);
        } else {
            return value;
        }
    },

    efEncodeDate: function (newValue, format) {
        if (newValue === Quasar.date.formatDate(Quasar.date.extractDate(newValue, format), format)) {
            return Quasar.date.formatDate(Quasar.date.extractDate(newValue, format), 'YYYY-MM-DD');
        } else {
            return newValue;
        }
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
    
    efSectionCanDelete : function(section) {
        if (section?.items) {
            for (let item of section.items) {
                if (!this.efItemCanDelete(item)) {
                    return false;
                }
            }
        }
        return true;
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
        let formData = this.vueDataParams(['editSection','editLabelText.longLabel']);
        formData.delete('vContext[editSection][haveSystemField]')//not modifiable

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
    
    efItemCanDelete : function(item) {
        if (item.type === 'FIELD') {
            return item.isSystem === false;
        } else if (item.type === 'BLOCK' && item?.items) {
            for (let subItem of item.items) {
                if (!this.efItemCanDelete(subItem)) {
                    return false;
                }
            }
        }
        return true;
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
        formData.delete('vContext[editItem][isSystem]')//not modifiable
        formData.delete('vContext[editItem][isList]')//not modifiable
        
        formData.append('sectionIndex', this.$data.componentStates.itemModal.sectionIndex);
        formData.append('doUpdateCode', !this.$data.componentStates.itemModal.codeModified);
        
        this.httpPostAjax(context + 'easyforms/designer/_refreshItem',formData);
    },


    efSaveEditItem : function() {
        let formData = this.vueDataParams(['editItem','editLabelText.label','editLabelText.tooltip','editLabelText.text']);
        formData.delete('vContext[editItem][type]')//not modifiable
        formData.delete('vContext[editItem][isSystem]')//not modifiable
        formData.delete('vContext[editItem][isList]')//not modifiable
       
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
	// * Component to handle computed fields
	// ****
	let vuiEasyFormsComputed = Vue.defineComponent({
		props: {
			modelValue: { type: Number, required: true },
			expression: { type: Number, required: true },
		},
		template: `<div>{{ expression }}</div>`,
		emits: ["update:modelValue"],
		watch: {
			expression: function(newVal) {
				this.$emit('update:modelValue', newVal);
			},
		}
	});
	event.detail.vuiAppInstance.component('vui-ef-computed',vuiEasyFormsComputed);
	
	
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
    
    let vuiEasyFormsMap = Vue.defineComponent({
        props: {
            modelValue: { type: Object, required: true },
            languages: { type: Array, default: () => [] },
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
                    <q-input label-slot stack-label orientation="vertical" class="col-5" :class="!isEmpty(param)?'v-field__required':''"
                            :label="valueLabel"
                            v-model="param.value"
                        ></q-input>
                    <span class="col-7">
                        <q-input v-for="(lang, index) in languages" 
                            label-slot stack-label orientation="vertical" 
                            :label="labelLabel + (languages.length > 1 ? ' (' + lang + ')' : '')"
                            :class="index === 0 && !isEmpty(param)?'v-field__required':''"
                            v-model="param.label[lang]"
                        ></q-input>
                    </span>
                </div>
            </div>
        `
        ,
        emits: ["update:modelValue"],
        created: function() {
            if(this.$props.modelValue) {
                this.$data.internalModel = this.$props.modelValue;
                if (!this.isLastEmpty(this.$props.modelValue)) {
                    this.$data.internalModel.push({label: {}, value: ''});
                }
            } else {
                this.$data.internalModel = [{label: {}, value: ''}];
            }
        },
        watch: {
            modelValue: function(newVal) {
                if(this.$props.modelValue) {
                    this.$data.internalModel = this.$props.modelValue;
                    if (!this.isLastEmpty(this.$props.modelValue)) {
                        this.$data.internalModel.push({label: {}, value: ''});
                    }
                } else {
                    this.$data.internalModel = [{label: {}, value: ''}];
                }
            },
            internalModel: {
                handler: function(newVal) {
                    if (!this.isLastEmpty(this.$data.internalModel)) {
                        this.$data.internalModel.push({label: {}, value: ''});
                    } else {
                        // keep only one empty at the end
                        while (this.$data.internalModel.length > 1 &&
                               this.isEmpty(this.$data.internalModel[this.$data.internalModel.length - 2])) {
                            this.$data.internalModel = this.$data.internalModel.slice(0, -1);
                        }
                    }
                    this.$emit('update:modelValue', this.$data.internalModel);
                },
                deep: true
            },
        },
        methods: {
            // Function to test if last item is empty
            isLastEmpty: function(o) {
                if (o == null || o.length === 0) {
                    return false;
                }
                const lastElem = o.slice(-1)[0];
                return this.isEmpty(lastElem);
            },
            isEmpty: function(o) {
                if (o.value?.length > 0) return false;
                for (let lang in o.label) {
                    if (o.label[lang].length > 0) return false;    
                }
                return true;
            }
        }
    });
    
    event.detail.vuiAppInstance.component('vui-ef-map',vuiEasyFormsMap);
});

