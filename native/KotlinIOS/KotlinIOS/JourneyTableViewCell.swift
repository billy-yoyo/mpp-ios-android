//
//  JourneyTableViewCell.swift
//  KotlinIOS
//
//  Created by Will Page on 11/09/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import UIKit

class JourneyTableViewCell: UITableViewCell {
    
    @IBOutlet weak var departureTime: UILabel!
    @IBOutlet weak var arrivalTime: UILabel!
    @IBOutlet weak var priceRange: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

}
