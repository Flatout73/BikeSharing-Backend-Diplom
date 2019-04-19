
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var bikeApi = Vue.resource('/bikes{/id}');

Vue.component('bike-form', {
    props: ['bikes', 'bikeAttr'],
    data: function() {
        return {
            name: '',
            id: ''
        }
    },
    watch: {
        bikeAttr: function(newVal, oldVal) {
            this.name = newVal.name;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="Write something" v-model="name" />' +
        '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var bike = { name: this.name };

            if (this.id) {
                bikeApi.update({id: this.id}, name).then(result =>
                result.json().then(data => {
                    var index = getIndex(this.bikes, data.id);
                this.bikes.splice(index, 1, data);
                this.name = ''
                this.id = ''
            })
            )
            } else {
                bikeApi.save({}, message).then(result =>
                result.json().then(data => {
                    this.bikes.push(data);
                this.name = ''
            })
            )
            }
        }
    }
});

Vue.component('bike-row', {
    props: ['bike', 'editMethod', 'bikes'],
    template: '<div>' +
        '<i>({{ bike.id }})</i> {{ bike.name }}' +
        '<span style="position: absolute; right: 0">' +
        '<input type="button" value="Edit" @click="edit" />' +
        '<input type="button" value="X" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function() {
            this.editMethod(this.bike);
        },
        del: function() {
            bikeApi.remove({id: this.bike.id}).then(result => {
                if (result.ok) {
                this.bikes.splice(this.bikes.indexOf(this.bike), 1)
            }
        })
        }
    }
});

Vue.component('bikes-list', {
    props: ['bikes'],
    data: function() {
        return {
            bike: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<bike-form :bikes="bikes" :bikeAttr="bike" />' +
        '<bike-row v-for="bike in bikes" :key="bike.id" :bike="bike" ' +
        ':editMethod="editMethod" :bikes="bikes" />' +
        '</div>',
    created: function() {
        bikeApi.get().then(result =>
            result.json().then(data =>
            data.forEach(bike => { this.bikes.push(bike) })
        )
    )
    },
    methods: {
        editMethod: function(bike) {
            this.bike = bike;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<bikes-list :bikes="bikes" />',
    data: {
        bikes: []
    }
});