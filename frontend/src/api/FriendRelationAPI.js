import {api} from "./index.js";

export const FriendRelationAPI = {
    getFriendList() {
        return api.get("friend-relation/list");
    },
    getOnlineFriendList() {
        return api.get("friend-relation/online");
    },

}