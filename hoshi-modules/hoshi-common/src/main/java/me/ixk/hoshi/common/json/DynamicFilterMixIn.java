/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.common.json;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * @author Otstar Lin
 * @date 2021/6/1 13:50
 */
@JsonFilter(DynamicFilterProvider.FILTER_NAME)
public class DynamicFilterMixIn {}
